package br.ifsp.stock_order.common.commands;

import br.ifsp.stock_order.common.events.OrderCreatedEvent;

import java.util.List;
import java.util.UUID;

public record ReserveStockCommand(
        UUID orderId,
        List<OrderCreatedEvent.OrderItemData> items
) {
}
