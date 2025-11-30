package br.ifsp.stock_order.customer.api.dto;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String name,
        String email
) {
}
