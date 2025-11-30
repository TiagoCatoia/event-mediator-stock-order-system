package br.ifsp.stock_order.order.api.dto;

import java.util.UUID;

public record CreateOrderItemRequest(
        UUID productId,
        Integer quantity
) {}