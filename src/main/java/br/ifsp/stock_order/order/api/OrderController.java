package br.ifsp.stock_order.order.api;

import br.ifsp.stock_order.order.api.dto.CreateOrderRequest;
import br.ifsp.stock_order.order.api.dto.OrderResponse;
import br.ifsp.stock_order.order.application.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
@Tag(name = "Order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok(orderService.findOrders());
    }

    @PostMapping
    public ResponseEntity<OrderResponse> CreateCustomer(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }
}
