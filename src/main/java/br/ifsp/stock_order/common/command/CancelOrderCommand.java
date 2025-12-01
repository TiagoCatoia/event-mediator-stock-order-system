package br.ifsp.stock_order.common.command;

import java.util.UUID;

public record CancelOrderCommand(
        UUID orderId,
        String message
) {
}