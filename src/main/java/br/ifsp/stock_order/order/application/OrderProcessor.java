package br.ifsp.stock_order.order.application;

import br.ifsp.stock_order.common.command.CancelOrderCommand;
import br.ifsp.stock_order.common.config.RabbitMQConfig;
import br.ifsp.stock_order.common.event.OrderCancelFailedEvent;
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

    @CircuitBreaker(name = "orderCancelCircuitBreaker", fallbackMethod = "fallbackCancelOrder")
    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_CANCEL)
    public void handleOrderCancel(CancelOrderCommand command) {
        System.out.println("PROCESSADOR ORDER recebeu uma ordem de cancelamento. Pedido: " + command.orderId());

        try {
            orderService.cancelOrder(command.orderId());
            System.out.println("PROCESSADOR ORDER concluiu o cancelamento do pedido: " + command.orderId());

        } catch (Exception e) {

            OrderCancelFailedEvent event = new OrderCancelFailedEvent(
                    command.orderId(),
                    e.getMessage());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_ORDER,
                    RabbitMQConfig.RK_ORDER_CANCEL,
                    command
            );
        }
    }

    public void fallbackCancelOrder(CancelOrderCommand command, Throwable throwable) {
        String subject = "Falha persistente ao cancelar pedido " + command.orderId();
        String body = "O pedido " + command.orderId() + " não pôde ser cancelado. Erro: " + throwable.getMessage();

        sendEmailAlert(subject, body);
    }

    private void sendEmailAlert(String subject, String body) {
        //
    }
}
