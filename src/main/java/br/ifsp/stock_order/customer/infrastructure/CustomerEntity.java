package br.ifsp.stock_order.customer.infrastructure;

import br.ifsp.stock_order.order.infrastructure.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "customer")
    private List<OrderEntity> order = new ArrayList<>();

    public CustomerEntity(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
