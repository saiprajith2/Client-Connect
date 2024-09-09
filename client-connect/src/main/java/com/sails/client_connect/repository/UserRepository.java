package com.sails.client_connect.repository;

import com.sails.client_connect.entity.RoleName;
import com.sails.client_connect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByRoles_Name(RoleName roleName);


    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles")
    List<User> findAllWithRoles();

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.user_id = :userId")
    Optional<User> findUserWithRolesById(int userId);
}
