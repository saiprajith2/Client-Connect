package com.sails.client_connect.dto;

import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.entity.RecurrencePattern;
import com.sails.client_connect.entity.Status;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {


    private String clientName;

    @NotBlank(message = "Task Title is required")
    private String taskTitle;

    @Size(max = 500, message = "Description can be up to 500 characters long")
    private String description;

    @NotNull(message = "Due Date and Time is required")
    @Future(message = "Due Date and Time should be in future")
    private LocalDateTime dueDateTime;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Status is required")
    private Status status;

    private RecurrencePattern recurrencePattern;

    private Long userId;

    @NotNull(message = "Customer Id is required")
    private Long customerId;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;
}
