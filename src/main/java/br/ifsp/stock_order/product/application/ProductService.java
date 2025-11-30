package br.ifsp.stock_order.product.application;

import br.ifsp.stock_order.product.api.dto.ProductResponse;
import br.ifsp.stock_order.product.infrastructure.ProductEntity;
import br.ifsp.stock_order.product.infrastructure.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findProducts() {
        List<ProductEntity> products = productRepository.findAll();

        return products.stream().map(c ->
                new ProductResponse(
                        c.getId(),
                        c.getName(),
                        c.getPrice()
                )).toList();
    }
}
