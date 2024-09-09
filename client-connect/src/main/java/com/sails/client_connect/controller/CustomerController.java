package com.sails.client_connect.controller;

import com.sails.client_connect.dto.CustomerDTO;
import com.sails.client_connect.dto.CustomerUpdateDTO;
import com.sails.client_connect.response.ApiResponse;
import com.sails.client_connect.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/customers")
@RequiredArgsConstructor
public class CustomerController {

    //dependency Injection
    private final CustomerService customerService;


    /**
     * Creates a new customer for the logged-in user.
     *
     * @param customerDTO Data for creating the customer.
     * @param session     HttpSession to retrieve the user ID.
     * @return The created customer in JSON format.
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(
            @Valid @RequestBody CustomerDTO customerDTO,
            HttpSession session) {

        // Retrieve the user_id from the session
        Long userId = (Long) session.getAttribute("userId");

        // Set the user_id in the customerDTO
        customerDTO.setUserId(userId);

        // Create the customer
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        ApiResponse<CustomerDTO> response = new ApiResponse<>(
                "Customer created successfully",
                HttpStatus.CREATED,
                createdCustomer
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves a specific customer by their ID for the logged-in user.
     *
     * @param id      The ID of the customer to retrieve.
     * @param session HttpSession to retrieve the user ID.
     * @return The customer matching the provided ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerUpdateDTO> getCustomerById(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        CustomerUpdateDTO customerDTO = customerService.getCustomerByIdAndUserId(id, userId);
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    /**
     * Retrieves all customers for the logged-in user.
     *
     * @param session HttpSession to retrieve the user ID.
     * @return A list of all customers for the user.
     */
    @GetMapping
    public ResponseEntity<List<CustomerUpdateDTO>> getAllCustomers(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        List<CustomerUpdateDTO> customers = customerService.getAllCustomersByUserId(userId);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Updates a specific customer by their ID for the logged-in user.
     *
     * @param id          The ID of the customer to update.
     * @param customerDTO Data for updating the customer.
     * @param session     HttpSession to retrieve the user ID.
     * @return The updated customer.
     */
    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerDTO customerDTO,
            HttpSession session) {

        // Retrieve the user_id from the session
        Long userId = (Long) session.getAttribute("userId");
        customerDTO.setUserId(userId);


        // Update the customer
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        ApiResponse<CustomerDTO> response = new ApiResponse<>(
                "Customer updated successfully",
                HttpStatus.OK,
                updatedCustomer
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Deletes a specific customer by their ID for the logged-in user.
     *
     * @param id      The ID of the customer to delete.
     * @param session HttpSession to retrieve the user ID.
     * @return A message indicating the customer was deleted successfully.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        customerService.deleteCustomer(id, userId);
        ApiResponse<Void> response = new ApiResponse<>(
                "Customer deleted successfully",
                HttpStatus.OK,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Searches for customers that match a given query for the logged-in user.
     *
     * @param query   The search query.
     * @param page    The page number for pagination.
     * @param size    The page size for pagination.
     * @param session HttpSession to retrieve the user ID.
     * @return A page of customers that match the query.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<CustomerUpdateDTO>> searchCustomers(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Page<CustomerUpdateDTO> customerPage = customerService.searchCustomers(query, page, size, userId);
        return ResponseEntity.ok(customerPage);

    }

    /**
     * Filters and sorts customers based on the provided parameters for the logged-in user.
     *
     * @param id          The ID of the customer.
     * @param firstName   The first name of the customer.
     * @param lastName    The last name of the customer.
     * @param email       The email address of the customer.
     * @param phoneNumber The phone number of the customer.
     * @param address     The address of the customer.
     * @param page        The page number for pagination.
     * @param size        The page size for pagination.
     * @param sortBy      The field by which to sort the customers.
     * @param sortDir     The direction in which to sort the customers.
     * @param session     HttpSession to retrieve the user ID.
     * @return A page of customers that match the filter and sort criteria.
     */
    @GetMapping("/filter-sort")
    public ResponseEntity<Page<CustomerUpdateDTO>> filterAndSortCustomers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String address,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Page<CustomerUpdateDTO> customerPage = customerService.filterAndSortCustomers(
                id, firstName, lastName, email, phoneNumber, address, page, size, sort, userId);
        return ResponseEntity.ok(customerPage);
    }

}

