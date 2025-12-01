package br.ifsp.stock_order.common.infrastructure;

import br.ifsp.stock_order.common.command.ReserveStockCommand;
import br.ifsp.stock_order.common.event.OrderCreatedEvent;
import br.ifsp.stock_order.common.event.StockReservationFailedEvent;
import br.ifsp.stock_order.common.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMediator {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_CREATED)
    public void onOrderCreated(OrderCreatedEvent event) {
        System.out.println("MEDIADOR recebeu o evento de pedido criado: " + event.orderId());

        ReserveStockCommand command = new ReserveStockCommand(
                event.orderId(),
                event.items());


        System.out.println("MEDIADOR enviando comando de reserva para o Estoque");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ORDER,
                RabbitMQConfig.RK_STOCK_RESERVE,
                command
        );
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_STOCK_FAILED)
    public void onStockFailed(StockReservationFailedEvent event) {
        System.out.println("MEDIADOR recebeu o evento de falha no stock: " + event.orderId());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ORDER,
                RabbitMQConfig.RK_ORDER_CANCEL,
                event
        );
    }
}
