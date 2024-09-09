package com.sails.client_connect.repository;


import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find a customer by its ID and associated user.
     *
     * @param id   Customer ID
     * @param user Associated User entity
     * @return Optional containing the Customer if found, otherwise empty
     */
    Optional<Customer> findByIdAndUser(Long id, User user);

    /**
     * Find all customers associated with a specific user.
     *
     * @param user Associated User entity
     * @return List of Customer entities
     */
    List<Customer> findAllByUser(User user);

    /**
     * Search for customers by query string within the context of a specific user.
     * The search checks the first name, last name, email, phone number, and address.
     *
     * @param query    Search query
     * @param user     Associated User entity
     * @param pageable Pageable object for pagination
     * @return Page of Customer entities that match the search criteria
     */
    @Query("SELECT c FROM Customer c WHERE " +
            "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "c.phoneNumber LIKE CONCAT('%', :query, '%') OR " +
            "LOWER(c.address) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "c.user = :user")
    Page<Customer> searchCustomersByUser(@Param("query") String query, @Param("user") User user, Pageable pageable);

    /**
     * Filter and sort customers based on various fields within the context of a specific user.
     * Any of the fields can be null, in which case they are not used in the filtering criteria.
     *
     * @param id          Customer ID
     * @param firstName   First Name
     * @param lastName    Last Name
     * @param email       Email
     * @param phoneNumber Phone Number
     * @param address     Address
     * @param pageable    Pageable object for pagination and sorting
     * @param user        Associated User entity
     * @return Page of Customer entities that match the filtering criteria
     */
    @Query("SELECT c FROM Customer c WHERE " +
            "(:id IS NULL OR c.id = :id) AND " +
            "(:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:phoneNumber IS NULL OR c.phoneNumber LIKE CONCAT('%', :phoneNumber, '%')) AND " +
            "(:address IS NULL OR LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))) AND " +
            "c.user = :user")
    Page<Customer> filterAndSortCustomersByUser(@Param("id") Long id,
                                                @Param("firstName") String firstName,
                                                @Param("lastName") String lastName,
                                                @Param("email") String email,
                                                @Param("phoneNumber") String phoneNumber,
                                                @Param("address") String address,
                                                Pageable pageable, @Param("user") User user);

}
