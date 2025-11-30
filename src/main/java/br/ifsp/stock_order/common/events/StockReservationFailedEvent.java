package br.ifsp.stock_order.common.events;

import java.util.UUID;

public record StockReservationFailedEvent(
        UUID orderId,
        String message
) {
}
