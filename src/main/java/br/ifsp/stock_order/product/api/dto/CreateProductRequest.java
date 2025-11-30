package br.ifsp.stock_order.product.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateProductRequest(
        @NotBlank(message = "Product name is required")
        String name,

        @NotNull(message = "Product price must not be null")
        @Positive(message = "Product price must be a positive value")
        Double price
) {
}
