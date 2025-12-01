package br.ifsp.stock_order.common.event;

import java.util.UUID;

public record StockReservationFailedEvent(
        UUID orderId,
        String message
) {
}
