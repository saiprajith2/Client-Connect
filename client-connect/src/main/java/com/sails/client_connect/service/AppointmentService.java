package com.sails.client_connect.service;

import com.sails.client_connect.dto.AppointmentDTO;
import com.sails.client_connect.entity.Appointment;
import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.RecurrencePattern;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.exception.ResourceNotFoundException;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.mapper.AppointmentMapper;
import com.sails.client_connect.repository.AppointmentRepository;
import com.sails.client_connect.repository.CustomerRepository;
import com.sails.client_connect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper mapper;
    private final AppointmentMapper appointmentMapper;

    /**
     * @param dto
     * @param userId creates an appointment by customer and user field or else
     *               return an exception resource not found exception
     */
    public AppointmentDTO createAppointment(AppointmentDTO dto, Long userId) {
        Appointment appointment = mapper.toEntity(dto);

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        appointment.setCustomer(customer);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        appointment.setUser(user);

        return mapper.toDto(appointmentRepository.save(appointment));
    }

    /**
     * @param id
     * @param dto
     * @param userId to update appointment by userID if user not found returns error message
     */

    public AppointmentDTO updateAppointment(Long id, AppointmentDTO dto, Long userId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Ensure the appointment belongs to the user
        if (!appointment.getUser().getUser_id().equals(userId)) {
            throw new ResourceNotFoundException("Appointment not found for the current user");
        }

        appointment.setTitle(dto.getTitle());
        appointment.setDescription(dto.getDescription());
        appointment.setStartDateTime(dto.getStartDateTime());
        appointment.setEndDateTime(dto.getEndDateTime());
        appointment.setLocation(dto.getLocation());
        appointment.setAttendees(dto.getAttendees());

        try {
            appointment.setRecurrencePattern(RecurrencePattern.valueOf(dto.getRecurrencePattern().name()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid recurrence pattern: " + dto.getRecurrencePattern());
        }

        return mapper.toDto(appointmentRepository.save(appointment));
    }

    /**
     * to delete an appointment by id or else return an error message
     *
     * @param id
     * @param userId
     */

    public void deleteAppointment(Long id, Long userId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.getUser().getUser_id().equals(userId)) {
            throw new ResourceNotFoundException("Appointment not found for the current user");
        }

        appointmentRepository.deleteById(id);
    }

    /**
     * to retrieve an appointment by id or else return a message if appointment not found
     *
     * @param id
     * @param userId
     */

    public AppointmentDTO getAppointment(Long id, Long userId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.getUser().getUser_id().equals(userId)) {
            throw new ResourceNotFoundException("Appointment not found for the current user");
        }

        return mapper.toDto(appointment);
    }

    /**
     * to get all appointment
     *
     * @param userId
     */

    public List<AppointmentDTO> getAllAppointments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return appointmentRepository.findAllByUser(user).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * to search an appointment by title description recurrence pattern
     *
     * @param query
     * @param page
     * @param size
     * @param userId
     */

    public Page<AppointmentDTO> searchAppointments(String query, int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointmentPage = appointmentRepository.searchAppointmentsByUser(query, userId, pageable);
        return appointmentPage.map(appointmentMapper::toDto);
    }

    /**
     * to filter and sort an appointment by title description location date and time
     */

    public Page<AppointmentDTO> filterAndSortAppointments(Long id, String title, String description,
                                                          String location, LocalDateTime startDateTime,
                                                          LocalDateTime endDateTime, int page, int size,
                                                          Sort sort, Long userId) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Appointment> appointmentPage = appointmentRepository.filterAndSortAppointmentsByUser(
                id, title, description, location, startDateTime, endDateTime, pageable, userId);
        return appointmentPage.map(appointmentMapper::toDto);
    }

}
