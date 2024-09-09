package com.sails.client_connect.dto;

import com.sails.client_connect.entity.RecurrencePattern;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Start Date Time is required")
    @Future(message = "Start Date Time should be in future")
    private LocalDateTime startDateTime;

    @NotNull(message = "End Date Time is required")
    @Future(message = "End Date Time should be in future")
    private LocalDateTime endDateTime;

    private String location;

    private List<String> attendees;

    @NotNull(message = "Customer Id is required")
    private Long customerId;

    @NotNull(message = "Recurrence Pattern is required")
    private RecurrencePattern recurrencePattern;

    @NotNull(message = "Task Id is Required")
    private Long taskId;

    private Long userId;
}
