package br.ifsp.stock_order.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Stock {
    private UUID id;

    private Integer quantity;
}
