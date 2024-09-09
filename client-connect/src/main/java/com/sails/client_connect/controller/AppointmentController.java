package com.sails.client_connect.controller;

import com.sails.client_connect.dto.AppointmentDTO;
import com.sails.client_connect.response.ApiResponse;
import com.sails.client_connect.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * @param appointmentDTO
     * @param session        To create an appointment
     */

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AppointmentDTO>> createAppointment(
            @Valid @RequestBody AppointmentDTO appointmentDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        appointmentDTO.setUserId(userId);
        AppointmentDTO createdAppointment = appointmentService.createAppointment(appointmentDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Appointment created successfully", HttpStatus.CREATED, createdAppointment));
    }

    /**
     * @param id
     * @param appointmentDTO
     * @param session        To update an appointment by ID
     */

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<AppointmentDTO>> updateAppointment(
            @PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        appointmentDTO.setUserId(userId);
        AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentDTO, userId);
        return ResponseEntity.ok(new ApiResponse<>("Appointment updated successfully", HttpStatus.OK, updatedAppointment));
    }

    /**
     * @param id
     * @param session To delete an appointment by ID
     */

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAppointment(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        appointmentService.deleteAppointment(id, userId);
        return ResponseEntity.ok(new ApiResponse<>("Appointment deleted successfully", HttpStatus.NO_CONTENT, null));
    }

    /**
     * To get appointment of a user by user id
     *
     * @param id
     * @param session
     */

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentDTO>> getAppointment(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        AppointmentDTO appointment = appointmentService.getAppointment(id, userId);
        return ResponseEntity.ok(new ApiResponse<>("Appointment retrieved successfully", HttpStatus.OK, appointment));
    }

    /**
     * To get all appointments
     *
     * @param session
     */

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> getAllAppointments(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments(userId);
        return ResponseEntity.ok(new ApiResponse<>("Appointments retrieved successfully", HttpStatus.OK, appointments));
    }

    /**
     * @param query
     * @param page
     * @param size
     * @param session To search an appointment
     */

    @GetMapping("/search")
    public ResponseEntity<Page<AppointmentDTO>> searchAppointments(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Page<AppointmentDTO> appointmentPage = appointmentService.searchAppointments(query, page, size, userId);
        return ResponseEntity.ok(appointmentPage);
    }

    /**
     * To filter an appointment based on title ,recurrence pattern
     */

    @GetMapping("/filter-sort")
    public ResponseEntity<Page<AppointmentDTO>> filterAndSortAppointments(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) LocalDateTime startDateTime,
            @RequestParam(required = false) LocalDateTime endDateTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDateTime") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Page<AppointmentDTO> appointmentPage = appointmentService.filterAndSortAppointments(
                id, title, description, location, startDateTime, endDateTime, page, size, sort, userId);
        return ResponseEntity.ok(appointmentPage);
    }
}
