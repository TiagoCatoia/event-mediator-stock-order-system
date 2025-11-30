package br.ifsp.stock_order.order.api.dto;

import br.ifsp.stock_order.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        OrderStatus status,
        Double totalPrice,
        LocalDateTime createdAt,
        UUID customerId,
        String customerName,
        List<OrderItemResponse> items
) {}