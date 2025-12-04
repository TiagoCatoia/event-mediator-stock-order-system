package br.ifsp.stock_order.common.config;

import br.ifsp.stock_order.customer.infrastructure.CustomerEntity;
import br.ifsp.stock_order.customer.infrastructure.CustomerRepository;
import br.ifsp.stock_order.product.infrastructure.ProductEntity;
import br.ifsp.stock_order.product.infrastructure.ProductRepository;
import br.ifsp.stock_order.product.infrastructure.StockEntity;
import br.ifsp.stock_order.product.infrastructure.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            StockRepository stockRepository
    ) {
        return args -> {

            createCustomerIfNotExists(customerRepository, "João Silva", "joao@gmail.com");
            createCustomerIfNotExists(customerRepository, "Maria Silva", "maria@gmail.com");

            createProductAndStockIfNotExists(productRepository, stockRepository,
                    "Notebook Gamer", 5000.00, 5);

            createProductAndStockIfNotExists(productRepository, stockRepository,
                    "Mouse Gamer", 150.00, 15);

            createProductAndStockIfNotExists(productRepository, stockRepository,
                    "Teclado Mecânico", 350.00, 30);
        };
    }

    private void createCustomerIfNotExists(CustomerRepository repo, String name, String email) {
        if (repo.findByEmail(email).isEmpty()) {
            CustomerEntity customer = new CustomerEntity(name, email);
            repo.save(customer);
        }
    }

    private void createProductAndStockIfNotExists(ProductRepository prodRepo, StockRepository stockRepo,
                                                  String name, Double price, Integer quantity) {
        if (prodRepo.findByName(name).isEmpty()) {
            ProductEntity product = new ProductEntity(name, price);
            product.setCreatedAt(LocalDateTime.now());
            ProductEntity savedProduct = prodRepo.save(product);

            StockEntity stock = new StockEntity(quantity, savedProduct);
            stock.setUpdatedAt(LocalDateTime.now());
            stockRepo.save(stock);
        }
    }
}
