package br.ifsp.stock_order.customer.application;

import br.ifsp.stock_order.customer.api.dto.CustomerResponse;
import br.ifsp.stock_order.customer.infrastructure.CustomerEntity;
import br.ifsp.stock_order.customer.infrastructure.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponse> findCustomers() {
        List<CustomerEntity> customers = customerRepository.findAll();
        return customers.stream().map(c ->
                new CustomerResponse(
                        c.getId(),
                        c.getName(),
                        c.getEmail()
                )).toList();
    }
}
