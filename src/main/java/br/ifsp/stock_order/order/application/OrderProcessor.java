package br.ifsp.stock_order.order.application;

import br.ifsp.stock_order.common.command.CancelOrderCommand;
import br.ifsp.stock_order.common.config.RabbitMQConfig;
import br.ifsp.stock_order.common.infrastructure.EmailService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProcessor {

    private final OrderService orderService;
    private final RabbitTemplate rabbitTemplate;
    private final EmailService emailService;
    private final Tracer tracer;

    @CircuitBreaker(name = "stock-api", fallbackMethod = "fallbackCancelOrder")
    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_CANCEL)
    public void handleOrderCancel(CancelOrderCommand command) {
        orderService.cancelOrder(command.orderId());
    }

    private void sendEmail(CancelOrderCommand command) {
        var emailSpan = tracer.nextSpan().name("sendEmail").start();
        try {
            String subject = "Falha persistente ao cancelar pedido " + command.orderId();
            String body = "O sistema de cancelamento está indisponível. Circuito aberto.";
            emailService.sendSimpleEmail("user@gmail.com", subject, body);

            emailSpan.tag("subject", subject);
            emailSpan.tag("recipient", "user@gmail.com");
            emailSpan.tag("status", "sent");
        } catch (Exception exception) {
            emailSpan.error(exception);
            throw exception;
        } finally {
            emailSpan.end();
        }
    }

    // Open
    public void fallbackCancelOrder(CancelOrderCommand command, CallNotPermittedException ex) {
        var fallbackOpenSpan = tracer.nextSpan().name("cancelOrderFallbackOpen").start();

        try {
            fallbackOpenSpan.tag("orderId", command.orderId().toString());
            fallbackOpenSpan.tag("circuitBreakerState", "open");

            sendEmail(command);
        }
        catch (Exception exception) {
            fallbackOpenSpan.error(exception);
            throw exception;
        } finally {
            fallbackOpenSpan.end();
        }
    }

    // Closed
    public void fallbackCancelOrder(CancelOrderCommand command, Throwable throwable) {
        var fallbackClosedSpan = tracer.nextSpan().name("cancelOrderFallbackClosed").start();

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_ORDER,
                    RabbitMQConfig.RK_ORDER_CANCEL,
                    command
            );

            fallbackClosedSpan.tag("orderId", command.orderId().toString());
            fallbackClosedSpan.tag("circuitBreakerState", "closed");
        } catch (Exception exception) {
            fallbackClosedSpan.error(exception);
            throw exception;
        } finally {
            fallbackClosedSpan.end();
        }
    }
}
