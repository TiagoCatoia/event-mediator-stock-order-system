package br.ifsp.stock_order.product.api;

import br.ifsp.stock_order.product.api.dto.CreateProductRequest;
import br.ifsp.stock_order.product.api.dto.ProductResponse;
import br.ifsp.stock_order.product.application.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
@Tag(name = "Product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.findProducts());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }
}
