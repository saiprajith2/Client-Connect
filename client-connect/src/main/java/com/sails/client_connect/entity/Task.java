package com.sails.client_connect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String clientName;

    @NotBlank(message = "Task Title is required")
    private String taskTitle;

    private String description;

    @NotNull(message = "Due Date and Time is required")
    @Future(message = "Due Date and Time should be in future")
    private LocalDateTime dueDateTime;

    @NotNull(message = "Priority is required")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_pattern")
    private RecurrencePattern recurrencePattern;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

}

