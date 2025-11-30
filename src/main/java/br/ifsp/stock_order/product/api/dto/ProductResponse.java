package br.ifsp.stock_order.product.api.dto;

import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        Double price
) {
}
