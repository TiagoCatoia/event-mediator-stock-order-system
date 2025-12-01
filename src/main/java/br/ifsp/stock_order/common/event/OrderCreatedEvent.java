package br.ifsp.stock_order.common.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        UUID customerId,
        Double totalPrice,
        String status,
        LocalDateTime createdAt,
        List<OrderItemData> items
) {
    public record OrderItemData(
            UUID productId,
            Integer quantity
    ) {}
}
