package br.ifsp.stock_order.order.api.dto;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        UUID customerId,
        List<CreateOrderItemRequest> items
) {}
