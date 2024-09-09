//package com.sails.client_connect.entity;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import lombok.*;
//import java.util.Set;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank(message = "First Name is required")
//    private String firstName;
//
//    @NotBlank(message = "Lastname is required")
//    private String lastName;
//
//    @NotBlank(message = "Email is required")
//    private String email;
//
//    @NotBlank(message = "Password is required")
//    private String password;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles;
//
//    @OneToMany(mappedBy = "assignedTo")
//    private Set<Task> tasks;
//
//}

package com.sails.client_connect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lead> leads;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Customer> customers;

    private LocalDateTime passwordLastSet;

}
