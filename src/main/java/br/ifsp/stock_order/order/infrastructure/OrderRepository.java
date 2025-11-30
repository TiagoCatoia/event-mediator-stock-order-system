package br.ifsp.stock_order.order.infrastructure;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    @Override
    @EntityGraph(attributePaths = {"orderItems", "customer", "orderItems.product"})
    List<OrderEntity> findAll();
}
