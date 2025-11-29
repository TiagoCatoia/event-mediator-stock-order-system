package br.ifsp.stock_order.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Customer {
    private UUID id;

    private String name;

    private String email;
}
