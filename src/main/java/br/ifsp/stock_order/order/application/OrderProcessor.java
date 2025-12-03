package br.ifsp.stock_order.order.application;

import br.ifsp.stock_order.common.command.CancelOrderCommand;
import br.ifsp.stock_order.common.config.RabbitMQConfig;
import br.ifsp.stock_order.common.infrastructure.EmailService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    @CircuitBreaker(name = "stock-api", fallbackMethod = "fallbackCancelOrder")
    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_CANCEL)
    public void handleOrderCancel(CancelOrderCommand command) {
        System.out.println("PROCESSADOR ORDER recebeu uma ordem de cancelamento. Pedido: " + command.orderId());
        orderService.cancelOrder(command.orderId());
    }

    // Open
    public void fallbackCancelOrder(CancelOrderCommand command, CallNotPermittedException ex) {
        System.err.println("Erros múltiplos ao processar o pedido, devolvendo dinheiro...");
//        String subject = "Falha persistente ao cancelar pedido " + command.orderId();
//        String body = "O sistema de cancelamento está indisponível. Circuito aberto.";
//        emailService.sendSimpleEmail("user@gmail.com", subject, body);
    }

    // Closed
    public void fallbackCancelOrder(CancelOrderCommand command, Throwable throwable) {
        System.err.println("Erro ao processar cancelamento do pedido, tentando novamente");
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ORDER,
                RabbitMQConfig.RK_ORDER_CANCEL,
                command
        );
    }
}
