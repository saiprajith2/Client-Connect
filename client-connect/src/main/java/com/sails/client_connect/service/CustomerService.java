package com.sails.client_connect.service;

import com.sails.client_connect.dto.CustomerDTO;
import com.sails.client_connect.dto.CustomerUpdateDTO;
import com.sails.client_connect.dto.CustomersFinancingDTO;
import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.mapper.CustomerMapper;
import com.sails.client_connect.repository.CustomerRepository;
import com.sails.client_connect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final UserRepository userRepository;

    /**
     * Get a list of customer names for financing purposes.
     *
     * @return List of CustomersFinancingDTO objects
     */
    public List<CustomersFinancingDTO> getCustomersNames() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toFinancingDto)
                .collect(Collectors.toList());
    }

    /**
     * Create a new customer.
     *
     * @param customerDTO Data Transfer Object containing customer details
     * @return Created CustomerDTO
     */
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        User user = userRepository.findById(customerDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        customer.setUser(user);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    /**
     * Retrieve a customer by their ID and associated user ID.
     *
     * @param id     Customer ID
     * @param userId User ID
     * @return CustomerUpdateDTO with customer details
     * @throws UserNotFoundException if the user is not found
     */
    public CustomerUpdateDTO getCustomerByIdAndUserId(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return customerRepository.findByIdAndUser(id, user)
                .map(customerMapper::toUpdateDto)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    /**
     * Retrieve all customers associated with a specific user ID.
     *
     * @param userId User ID
     * @return List of CustomerUpdateDTO objects
     * @throws UserNotFoundException if the user is not found
     */
    public List<CustomerUpdateDTO> getAllCustomersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return customerRepository.findAllByUser(user).stream()
                .map(customerMapper::toUpdateDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all customers.
     *
     * @return List of CustomerDTO objects
     */
    public List<CustomerUpdateDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toUpdateDto)
                .collect(Collectors.toList());
    }


    /**
     * Update customer details.
     *
     * @param id          Customer ID
     * @param customerDTO Data Transfer Object containing updated customer details
     * @return Updated CustomerDTO
     */
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));

        // Update the customer's fields if they are present in the DTO
        if (customerDTO.getFirstName() != null) {
            customer.setFirstName(customerDTO.getFirstName());
        }
        if (customerDTO.getLastName() != null) {
            customer.setLastName(customerDTO.getLastName());
        }
        if (customerDTO.getEmail() != null) {
            customer.setEmail(customerDTO.getEmail());
        }
        if (customerDTO.getAddress() != null) {
            customer.setAddress(customerDTO.getAddress());
        }
        if (customerDTO.getPhoneNumber() != null) {
            customer.setPhoneNumber(customerDTO.getPhoneNumber());
        }

        // Update the user if userId is present in the DTO
        if (customerDTO.getUserId() != null) {
            User user = userRepository.findById(customerDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + customerDTO.getUserId()));
            customer.setUser(user);
        }


        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(updatedCustomer);
    }

    /**
     * Delete a customer by their ID and associated user ID.
     *
     * @param id     Customer ID
     * @param userId User ID
     * @throws UserNotFoundException if the user is not found
     */
    public void deleteCustomer(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Customer customer = customerRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id + " for user " + userId));
        customerRepository.delete(customer);
    }

    /**
     * Search for customers based on a query string, paginated.
     *
     * @param query  Search query
     * @param page   Page number
     * @param size   Page size
     * @param userId User ID
     * @return Page of CustomerUpdateDTO objects
     * @throws UserNotFoundException if the user is not found
     */
    public Page<CustomerUpdateDTO> searchCustomers(String query, int page, int size, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size);

        // Debugging
        System.out.println("Query: " + query);
        System.out.println("Page: " + page + ", Size: " + size);
        System.out.println("User: " + user.getEmail());

        Page<Customer> customerPage = customerRepository.searchCustomersByUser(query, user, pageable);
        return customerPage.map(customerMapper::toUpdateDto);
    }

    /**
     * Filter and sort customers based on various fields.
     *
     * @param id          Customer ID
     * @param firstName   First Name
     * @param lastName    Last Name
     * @param email       Email
     * @param phoneNumber Phone Number
     * @param address     Address
     * @param page        Page number
     * @param size        Page size
     * @param sort        Sorting criteria
     * @param userId      User ID
     * @return Page of CustomerUpdateDTO objects
     * @throws UserNotFoundException if the user is not found
     */
    public Page<CustomerUpdateDTO> filterAndSortCustomers(Long id, String firstName, String lastName,
                                                          String email, String phoneNumber, String address,
                                                          int page, int size, Sort sort, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Customer> customerPage = customerRepository.filterAndSortCustomersByUser(
                id, firstName, lastName, email, phoneNumber, address, pageable, user);
        return customerPage.map(customerMapper::toUpdateDto);
    }

}
