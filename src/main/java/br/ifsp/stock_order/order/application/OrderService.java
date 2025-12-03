package br.ifsp.stock_order.order.application;

import br.ifsp.stock_order.common.command.CancelOrderCommand;
import br.ifsp.stock_order.common.event.OrderCreatedEvent;
import br.ifsp.stock_order.customer.infrastructure.CustomerRepository;
import br.ifsp.stock_order.common.config.RabbitMQConfig;
import br.ifsp.stock_order.order.api.dto.CreateOrderRequest;
import br.ifsp.stock_order.order.api.dto.OrderItemResponse;
import br.ifsp.stock_order.order.api.dto.OrderResponse;
import br.ifsp.stock_order.order.domain.OrderStatus;
import br.ifsp.stock_order.order.infrastructure.OrderEntity;
import br.ifsp.stock_order.order.infrastructure.OrderItemEntity;
import br.ifsp.stock_order.order.infrastructure.OrderRepository;
import br.ifsp.stock_order.product.infrastructure.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<OrderResponse> findOrders(){
        List<OrderEntity> orders = orderRepository.findAll();

        return orders.stream().map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    public void cancelOrder(UUID orderId) {
        throw new RuntimeException("Simulando falha no cancelamento do pedido");
//        OrderEntity order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
//
//        order.setStatus(OrderStatus.CANCELLED);
//        orderRepository.save(order);
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        var customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + request.customerId()));

        var order = new OrderEntity();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderItems(new ArrayList<>());

        double totalOrderPrice = 0.0;

        for (var itemRequest : request.items()) {
            var product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + itemRequest.productId()));

            double itemTotal = product.getPrice() * itemRequest.quantity();

            var orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setUnitPrice(itemTotal);
            orderItem.setOrderStatus(OrderStatus.CREATED);

            order.getOrderItems().add(orderItem);
            totalOrderPrice += itemTotal;
        }

        order.setTotalPrice(totalOrderPrice);
        OrderEntity savedOrder = orderRepository.save(order);

        publishOrderCreatedEvent(savedOrder);

        return toOrderResponse(savedOrder);
    }

    private void publishOrderCreatedEvent(OrderEntity savedOrder) {
        List<OrderCreatedEvent.OrderItemData> itemsData = savedOrder.getOrderItems().stream()
                .map(item -> new OrderCreatedEvent.OrderItemData(
                        item.getProduct().getId(),
                        item.getQuantity()
                )).toList();

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getCustomer().getId(),
                savedOrder.getTotalPrice(),
                savedOrder.getStatus().toString(),
                savedOrder.getCreatedAt(),
                itemsData
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ORDER,
                RabbitMQConfig.RK_ORDER_CREATED,
                event
        );
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
