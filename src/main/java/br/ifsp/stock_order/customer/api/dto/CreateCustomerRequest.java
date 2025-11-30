package br.ifsp.stock_order.customer.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(
        @NotBlank(message = "Customer name is required")
        String name,


        @NotBlank(message = "Customer email is required")
        @Email(message = "Email should be valid")
        String email
) {
}
