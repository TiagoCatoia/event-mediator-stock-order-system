package br.ifsp.stock_order.common.command;

import br.ifsp.stock_order.common.event.OrderCreatedEvent;

import java.util.List;
import java.util.UUID;

public record ReserveStockCommand(
        UUID orderId,
        List<OrderCreatedEvent.OrderItemData> items
) {
}
