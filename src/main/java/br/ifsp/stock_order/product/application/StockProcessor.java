package br.ifsp.stock_order.product.application;

import br.ifsp.stock_order.common.commands.ReserveStockCommand;
import br.ifsp.stock_order.common.events.StockReservationFailedEvent;
import br.ifsp.stock_order.infrastructure.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockProcessor {

    private final StockService stockService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_STOCK_RESERVE)
    public void handleStockReservation(ReserveStockCommand command) {
        System.out.println("PROCESSADOR STOCK recebeu um comando. Pedido: " + command.orderId());

        try {
            stockService.reserveStock(command.orderId(), command.items());
            System.out.println("PROCESSADOR STOCK concluiu a reserva do pedido " + command.orderId());

        } catch (Exception e) {

            StockReservationFailedEvent event = new StockReservationFailedEvent(
                    command.orderId(),
                    e.getMessage());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_ORDER,
                    RabbitMQConfig.RK_STOCK_FAILED,
                    event
            );
        }
    }
}
