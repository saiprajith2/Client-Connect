package com.sails.client_connect.controller;

import com.sails.client_connect.dto.CustomerDTO;
import com.sails.client_connect.dto.CustomerUpdateDTO;
import com.sails.client_connect.response.ApiResponse;
import com.sails.client_connect.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CustomerController customerController;


    /**
     * Test case for creating a customer.
     */
    @Test
    void testCreateCustomer() {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1L);
        CustomerDTO customerDTO = new CustomerDTO();
        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        // Act
        ResponseEntity<ApiResponse<CustomerDTO>> response = customerController.createCustomer(customerDTO, session);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Customer created successfully", response.getBody().getMessage());
        assertEquals(customerDTO, response.getBody().getData());
    }

    /**
     * Test case for getting a customer by ID.
     */
    @Test
    void testGetCustomerById() {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1L);
        CustomerUpdateDTO customerUpdateDTO = new CustomerUpdateDTO();
        when(customerService.getCustomerByIdAndUserId(anyLong(), anyLong())).thenReturn(customerUpdateDTO);

        // Act
        ResponseEntity<CustomerUpdateDTO> response = customerController.getCustomerById(1L, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerUpdateDTO, response.getBody());
    }

    /**
     * Test case for getting all customers.
     */
    @Test
    void testGetAllCustomers() {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1L);
        List<CustomerUpdateDTO> customers = Collections.singletonList(new CustomerUpdateDTO());
        when(customerService.getAllCustomersByUserId(anyLong())).thenReturn(customers);

        // Act
        ResponseEntity<List<CustomerUpdateDTO>> response = customerController.getAllCustomers(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customers, response.getBody());
    }

    /**
     * Test case for updating a customer.
     */
    @Test
    void testUpdateCustomer() {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1L);
        CustomerDTO customerDTO = new CustomerDTO();
        when(customerService.updateCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(customerDTO);

        // Act
        ResponseEntity<ApiResponse<CustomerDTO>> response = customerController.updateCustomer(1L, customerDTO, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Customer updated successfully", response.getBody().getMessage());
        assertEquals(customerDTO, response.getBody().getData());
    }

    /**
     * Test case for deleting a customer.
     */
    @Test
    void testDeleteCustomer() {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1L);

        // Act
        ResponseEntity<ApiResponse<Void>> response = customerController.deleteCustomer(1L, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Customer deleted successfully", response.getBody().getMessage());
    }

    /**
     * Test case for searching customers.
     */
    @Test
    void testSearchCustomers() {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1L);
        Page<CustomerUpdateDTO> customerPage = new PageImpl<>(Collections.singletonList(new CustomerUpdateDTO()));
        when(customerService.searchCustomers(anyString(), anyInt(), anyInt(), anyLong()))
                .thenReturn(customerPage);

        // Act
        ResponseEntity<Page<CustomerUpdateDTO>> response = customerController.searchCustomers(
                "query", 0, 10, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerPage, response.getBody());
    }

    /**
     * Test case for filtering and sorting customers.
     */
    @Test
    void testFilterAndSortCustomers() {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1L);
        Page<CustomerUpdateDTO> customerPage = new PageImpl<>(Collections.singletonList(new CustomerUpdateDTO()));
        when(customerService.filterAndSortCustomers(
                anyLong(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyInt(), anyInt(), any(Sort.class), anyLong()))
                .thenReturn(customerPage);

        // Act
        ResponseEntity<Page<CustomerUpdateDTO>> response = customerController.filterAndSortCustomers(
                1L, "firstName", "lastName", "email", "phoneNumber", "address",
                0, 10, "firstName", "asc", session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerPage, response.getBody());
    }
}
