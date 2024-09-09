package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerRepositoryTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerRepositoryTest customerRepositoryTest;


    /**
     * Test for finding a customer by ID and user.
     */
    @Test
    void testFindByIdAndUser() {
        User user = new User();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUser(user);

        when(customerRepository.findByIdAndUser(any(Long.class), any(User.class)))
                .thenReturn(Optional.of(customer));

        Optional<Customer> result = customerRepository.findByIdAndUser(1L, user);
        assertEquals(customer, result.orElse(null));
    }

    /**
     * Test for finding all customers by user.
     */
    @Test
    void testFindAllByUser() {
        User user = new User(); // Initialize user
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        customer1.setUser(user);
        customer2.setUser(user);

        when(customerRepository.findAllByUser(any(User.class)))
                .thenReturn(Arrays.asList(customer1, customer2));

        List<Customer> result = customerRepository.findAllByUser(user);
        assertEquals(2, result.size());
        assertEquals(customer1, result.get(0));
        assertEquals(customer2, result.get(1));
    }

    /**
     * Test for searching customers by user.
     */
    @Test
    void testSearchCustomersByUser() {
        User user = new User();
        Pageable pageable = Pageable.ofSize(10);
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        Page<Customer> customerPage = new PageImpl<>(Arrays.asList(customer1, customer2), pageable, 2);

        when(customerRepository.searchCustomersByUser(any(String.class), any(User.class), any(Pageable.class)))
                .thenReturn(customerPage);

        Page<Customer> result = customerRepository.searchCustomersByUser("query", user, pageable);
        assertEquals(2, result.getContent().size());
        assertEquals(customer1, result.getContent().get(0));
        assertEquals(customer2, result.getContent().get(1));
    }

    /**
     * Test for filtering and sorting customers by user.
     */
    @Test
    void testFilterAndSortCustomersByUser() {
        User user = new User();
        Customer customer = new Customer();
        customer.setUser(user);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "firstName"));
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customer), pageable, 1);

        when(customerRepository.filterAndSortCustomersByUser(
                anyLong(), anyString(), anyString(), anyString(), anyString(), anyString(), any(Pageable.class), any(User.class)))
                .thenReturn(customerPage);

        Page<Customer> result = customerRepository.filterAndSortCustomersByUser(
                1L, "John", "Doe", "john.doe@example.com", "1234567890", "123 Main St", pageable, user);

        assertEquals(1, result.getTotalElements());
        assertEquals(customer, result.getContent().get(0));
    }
}
