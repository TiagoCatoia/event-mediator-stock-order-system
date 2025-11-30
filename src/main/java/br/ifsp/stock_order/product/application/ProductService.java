package br.ifsp.stock_order.product.application;

import br.ifsp.stock_order.product.api.dto.CreateProductRequest;
import br.ifsp.stock_order.product.api.dto.ProductResponse;
import br.ifsp.stock_order.product.infrastructure.ProductEntity;
import br.ifsp.stock_order.product.infrastructure.ProductRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                        c.getPrice(),
                        c.getCreatedAt()
                )).toList();
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        Optional<ProductEntity> existingProduct = productRepository.findByName(request.name());
        if (existingProduct.isPresent()) {
            throw new EntityExistsException("Product name already exists: " + request.name());
        }

        ProductEntity product = new ProductEntity(
                request.name(),
                request.price()
        );

        productRepository.save(product);
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCreatedAt()
        );
    }
}
