package com.sails.client_connect.service;

import com.sails.client_connect.dto.CustomerDTO;
import com.sails.client_connect.dto.CustomerUpdateDTO;
import com.sails.client_connect.dto.CustomersFinancingDTO;
import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.mapper.CustomerMapper;
import com.sails.client_connect.repository.AppointmentRepository;
import com.sails.client_connect.repository.CustomerRepository;
import com.sails.client_connect.repository.TaskRepository;
import com.sails.client_connect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@SpringBootTest
public class CustomerServiceTest {

    // Mock the CustomerRepository dependency
    @Mock
    private CustomerRepository customerRepository;

    // Mock the CustomerMapper dependency
    @Mock
    private CustomerMapper customerMapper;

    // Mock the UserRepository dependency
    @Mock
    private UserRepository userRepository;

    // Mock the TaskRepository dependency
    @Mock
    private TaskRepository taskRepository;

    // Mock the AppointmentRepository dependency
    @Mock
    private AppointmentRepository appointmentRepository;

    // Inject the mocks into the CustomerService instance
    @InjectMocks
    private CustomerService customerService;

    /**
     * Test the retrieval of customer names.
     */
    @Test
    void testGetCustomersNames() {
        // Arrange
        List<Customer> customers = List.of(new Customer(), new Customer());
        List<CustomersFinancingDTO> dtos = List.of(new CustomersFinancingDTO(), new CustomersFinancingDTO());
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toFinancingDto(any(Customer.class))).thenReturn(new CustomersFinancingDTO());

        // Act
        List<CustomersFinancingDTO> result = customerService.getCustomersNames();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerRepository).findAll();
    }

    /**
     * Test the creation of a new customer.
     */
    @Test
    void testCreateCustomer() {
        // Arrange
        CustomerDTO dto = new CustomerDTO();
        dto.setUserId(1L);
        Customer customer = new Customer();
        User user = new User();
        Customer savedCustomer = new Customer();
        when(customerMapper.toEntity(any(CustomerDTO.class))).thenReturn(customer);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(dto);

        // Act
        CustomerDTO result = customerService.createCustomer(dto);

        // Assert
        assertNotNull(result);
        verify(customerRepository).save(any(Customer.class));
    }

    /**
     * Test retrieval of a customer by ID and user ID.
     */
    @Test
    void testGetCustomerByIdAndUserId() {
        // Arrange
        Customer customer = new Customer();
        CustomerUpdateDTO dto = new CustomerUpdateDTO();
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(customerRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(customer));
        when(customerMapper.toUpdateDto(any(Customer.class))).thenReturn(dto);

        // Act
        CustomerUpdateDTO result = customerService.getCustomerByIdAndUserId(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByIdAndUser(anyLong(), any(User.class));
    }

    /**
     * Test retrieval of all customers by user ID.
     */
    @Test
    void testGetAllCustomersByUserId() {
        // Arrange
        User user = new User();
        List<Customer> customers = List.of(new Customer(), new Customer());
        List<CustomerUpdateDTO> dtos = List.of(new CustomerUpdateDTO(), new CustomerUpdateDTO());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(customerRepository.findAllByUser(any(User.class))).thenReturn(customers);
        when(customerMapper.toUpdateDto(any(Customer.class))).thenReturn(new CustomerUpdateDTO());

        // Act
        List<CustomerUpdateDTO> result = customerService.getAllCustomersByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerRepository).findAllByUser(any(User.class));
    }

    /**
     * Test retrieval of all customers.
     */
    @Test
    void testGetAllCustomers() {
        // Arrange
        List<Customer> customers = List.of(new Customer(), new Customer());
        List<CustomerDTO> dtos = List.of(new CustomerDTO(), new CustomerDTO());
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(new CustomerDTO());

        // Act
        List<CustomerUpdateDTO> result = customerService.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerRepository).findAll();
    }

    /**
     * Test updating an existing customer.
     */
    @Test
    void testUpdateCustomer() {
        // Arrange
        CustomerDTO dto = new CustomerDTO();
        dto.setFirstName("Updated Name");
        Customer customer = new Customer();
        Customer updatedCustomer = new Customer();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(dto);

        // Act
        CustomerDTO result = customerService.updateCustomer(1L, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getFirstName());
        verify(customerRepository).save(any(Customer.class));
    }

    /**
     * Test deletion of a customer.
     */
    @Test
    void testDeleteCustomer() {
        // Arrange
        User user = new User();
        Customer customer = new Customer();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(customerRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(customer));

        // Act
        customerService.deleteCustomer(1L, 1L);

        // Assert
        verify(customerRepository).delete(any(Customer.class));
    }

    /**
     * Test searching customers with pagination.
     */
    @Test
    void testSearchCustomers() {
        // Arrange
        Customer customer = new Customer();
        CustomerUpdateDTO dto = new CustomerUpdateDTO();
        Page<Customer> customerPage = new PageImpl<>(List.of(customer));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(customerRepository.searchCustomersByUser(anyString(), any(User.class), any(Pageable.class)))
                .thenReturn(customerPage);
        when(customerMapper.toUpdateDto(any(Customer.class))).thenReturn(dto);

        // Act
        Page<CustomerUpdateDTO> result = customerService.searchCustomers("query", 0, 10, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    /**
     * Test filtering and sorting customers with pagination.
     */
    @Test
    void testFilterAndSortCustomers() {
        // Arrange
        Customer customer = new Customer();
        CustomerUpdateDTO dto = new CustomerUpdateDTO();
        Page<Customer> customerPage = new PageImpl<>(List.of(customer));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(customerRepository.filterAndSortCustomersByUser(
                anyLong(), anyString(), anyString(), anyString(), anyString(), anyString(), any(Pageable.class), any(User.class)))
                .thenReturn(customerPage);
        when(customerMapper.toUpdateDto(any(Customer.class))).thenReturn(dto);

        // Act
        Page<CustomerUpdateDTO> result = customerService.filterAndSortCustomers(
                1L, "firstName", "lastName", "email", "phoneNumber", "address", 0, 10, Sort.by("firstName"), 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
