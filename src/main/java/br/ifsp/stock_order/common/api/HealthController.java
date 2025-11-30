package br.ifsp.stock_order.common.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health")
public class HealthController {
    @GetMapping
    public ResponseEntity<Void> getHealthStatus() {
        return ResponseEntity.ok().build();
    }
}
