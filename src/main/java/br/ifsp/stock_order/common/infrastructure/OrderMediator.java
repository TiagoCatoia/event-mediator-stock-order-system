package br.ifsp.stock_order.common.infrastructure;

import br.ifsp.stock_order.common.command.CancelOrderCommand;
import br.ifsp.stock_order.common.command.ReserveStockCommand;
import br.ifsp.stock_order.common.event.OrderCreatedEvent;
import br.ifsp.stock_order.common.event.StockReservationFailedEvent;
import br.ifsp.stock_order.common.config.RabbitMQConfig;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMediator {

    private final RabbitTemplate rabbitTemplate;
    private final Tracer tracer;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_CREATED)
    public void onOrderCreated(OrderCreatedEvent event) {
        var orderCreatedSpan = tracer.nextSpan().name("orderCreated").start();
        try {
            ReserveStockCommand command = new ReserveStockCommand(
                    event.orderId(),
                    event.items());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_ORDER,
                    RabbitMQConfig.RK_STOCK_RESERVE,
                    command
            );
        } catch (Exception exception){
            orderCreatedSpan.error(exception);
            throw exception;
        } finally {
            orderCreatedSpan.end();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_STOCK_FAILED)
    public void onStockFailed(StockReservationFailedEvent event) {
        var stockFailedSpan = tracer.nextSpan().name("stockFailed").start();
        try {
            CancelOrderCommand command = new CancelOrderCommand(
                    event.orderId(),
                    event.message()
            );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_ORDER,
                    RabbitMQConfig.RK_ORDER_CANCEL,
                    command
            );

            stockFailedSpan.tag("orderId", event.orderId().toString());
            stockFailedSpan.tag("message", event.message());
        } catch (Exception exception){
            stockFailedSpan.error(exception);
            throw exception;
        } finally {
            stockFailedSpan.end();
        }
    }
}
