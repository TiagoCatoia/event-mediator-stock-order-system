package br.ifsp.stock_order.order.api.dto;

import br.ifsp.stock_order.order.domain.OrderStatus;

import java.util.UUID;

public record OrderItemResponse(
        UUID id,
        Double unitPrice,
        UUID productId
) {}
