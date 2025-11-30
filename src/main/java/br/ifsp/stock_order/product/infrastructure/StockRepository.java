package br.ifsp.stock_order.product.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockRepository extends JpaRepository<StockEntity, UUID> {
}
