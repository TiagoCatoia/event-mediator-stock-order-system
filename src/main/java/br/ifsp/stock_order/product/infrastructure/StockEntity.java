package br.ifsp.stock_order.product.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private Integer quantity;

    private LocalDateTime updatedAt;

    @OneToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private ProductEntity product;

    public StockEntity(Integer quantity, ProductEntity product, LocalDateTime updatedAt) {
        this.quantity = quantity;
        this.product = product;
        this.updatedAt = updatedAt;
    }

    public StockEntity(Integer quantity, ProductEntity product) {
        this(quantity, product, LocalDateTime.now());
    }
}
