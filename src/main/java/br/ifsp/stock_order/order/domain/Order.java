package br.ifsp.stock_order.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Order {
    private UUID id;

    private OrderStatus status;

    private Double totalPrice;
}
