package br.ifsp.stock_order.mediator;

import br.ifsp.stock_order.common.commands.ReserveStockCommand;
import br.ifsp.stock_order.common.events.OrderCreatedEvent;
import br.ifsp.stock_order.infrastructure.RabbitMQConfig;
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
}
