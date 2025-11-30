package br.ifsp.stock_order.customer.api;

import br.ifsp.stock_order.customer.api.dto.CreateCustomerRequest;
import br.ifsp.stock_order.customer.api.dto.CustomerResponse;
import br.ifsp.stock_order.customer.application.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
@Tag(name = "Customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.findCustomers());
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CreateCustomerRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }
}
