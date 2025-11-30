package br.ifsp.stock_order.order.application;

import br.ifsp.stock_order.customer.infrastructure.CustomerRepository;
import br.ifsp.stock_order.order.api.dto.CreateOrderRequest;
import br.ifsp.stock_order.order.api.dto.OrderItemResponse;
import br.ifsp.stock_order.order.api.dto.OrderResponse;
import br.ifsp.stock_order.order.domain.OrderStatus;
import br.ifsp.stock_order.order.infrastructure.OrderEntity;
import br.ifsp.stock_order.order.infrastructure.OrderItemEntity;
import br.ifsp.stock_order.order.infrastructure.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public List<OrderResponse> findOrders(){
        List<OrderEntity> orders = orderRepository.findAll();

        return orders.stream().map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        var customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + request.customerId()));

        var order = new OrderEntity();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderItems(new ArrayList<>());

        double totalOrderPrice = 0.0;

//        for (var itemRequest : request.items()) {
//            var product = productRepository.findById(itemRequest.productId())
//                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + itemRequest.productId()));
//
//            double itemTotal = product.getPrice() * itemRequest.quantity();
//
//            var orderItem = new OrderItemEntity();
//            orderItem.setOrder(order);
//            orderItem.setProduct(product);
//            orderItem.setUnitPrice(itemTotal);
//
//            order.getOrderItems().add(orderItem);
//            totalOrderPrice += itemTotal;
//        }

        order.setTotalPrice(totalOrderPrice);
        OrderEntity savedOrder = orderRepository.save(order);

        return toOrderResponse(savedOrder);
    }

    private OrderResponse toOrderResponse(OrderEntity entity) {
        List<OrderItemResponse> itemsDto = entity.getOrderItems().stream()
                .map(this::toOrderItemResponse)
                .toList();

        return new OrderResponse(
                entity.getId(),
                entity.getStatus(),
                entity.getTotalPrice(),
                entity.getCreatedAt(),
                entity.getCustomer().getId(),
                entity.getCustomer().getName(),
                itemsDto
        );
    }

    private OrderItemResponse toOrderItemResponse(OrderItemEntity entity) {
        return new OrderItemResponse(
                entity.getId(),
                entity.getUnitPrice(),
                entity.getProduct().getId()
        );
    }
}
