package br.ifsp.stock_order.common.event;

import java.util.UUID;

public record OrderCancelFailedEvent(
        UUID orderId,
        String message
) {
}
