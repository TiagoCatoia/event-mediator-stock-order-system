package br.ifsp.stock_order.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderItem {
    private UUID id;

    private int quantity;

    private Double unitPrice;
}
