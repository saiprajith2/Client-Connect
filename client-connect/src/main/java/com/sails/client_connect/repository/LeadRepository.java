package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Lead;
import com.sails.client_connect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    // Find a lead by its ID and associated user ID
    Optional<Lead> findByIdAndUser(Long id, User user);

    // Find all leads associated with a specific user ID
    List<Lead> findAllByUser(User user);
}
