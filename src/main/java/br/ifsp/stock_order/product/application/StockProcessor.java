package br.ifsp.stock_order.product.application;

import br.ifsp.stock_order.common.command.ReserveStockCommand;
import br.ifsp.stock_order.common.event.StockReservationFailedEvent;
import br.ifsp.stock_order.common.config.RabbitMQConfig;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockProcessor {

    private final StockService stockService;
    private final RabbitTemplate rabbitTemplate;
    private final Tracer tracer;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_STOCK_RESERVE)
    public void handleStockReservation(ReserveStockCommand command) {
        var stockReserveSpan = tracer.nextSpan().name("reserveStock").start();
        try {
            stockService.reserveStock(command.orderId(), command.items());

            stockReserveSpan.tag("orderId", command.orderId().toString());
            stockReserveSpan.tag("items", String.valueOf(command.items().size()));
        } catch (Exception exception) {
            stockReserveSpan.error(exception);

            StockReservationFailedEvent event = new StockReservationFailedEvent(
                    command.orderId(),
                    exception.getMessage());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_ORDER,
                    RabbitMQConfig.RK_STOCK_FAILED,
                    event
            );
        } finally {
            stockReserveSpan.end();
        }
    }
}
