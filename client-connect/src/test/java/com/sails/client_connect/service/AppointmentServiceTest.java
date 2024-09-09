package com.sails.client_connect.service;

import com.sails.client_connect.dto.AppointmentDTO;
import com.sails.client_connect.entity.Appointment;
import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.mapper.AppointmentMapper;
import com.sails.client_connect.repository.AppointmentRepository;
import com.sails.client_connect.repository.CustomerRepository;
import com.sails.client_connect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentService appointmentService;


    /**
     * To test createAppointment
     */
    @Test
    void testCreateAppointment() {
        // Arrange
        AppointmentDTO dto = new AppointmentDTO();
        dto.setCustomerId(1L);

        Appointment appointment = new Appointment();
        Customer customer = new Customer();
        User user = new User();

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(appointmentMapper.toEntity(any(AppointmentDTO.class))).thenReturn(appointment);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toDto(any(Appointment.class))).thenReturn(dto);

        // Act
        AppointmentDTO result = appointmentService.createAppointment(dto, 1L);

        // Assert
        assertNotNull(result);
        verify(appointmentRepository).save(appointment);
    }

    /**
     * To test GetAppointment
     */

    @Test
    void testGetAppointment() {
        // Arrange
        User user = new User();
        user.setUser_id(1L);  // Ensure user_id is set

        Appointment appointment = new Appointment();
        appointment.setUser(user);

        AppointmentDTO dto = new AppointmentDTO();
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toDto(any(Appointment.class))).thenReturn(dto);

        // Act
        AppointmentDTO result = appointmentService.getAppointment(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(dto, result);
    }

    /**
     * To Test GetAllAppointment
     */

    @Test
    void testGetAllAppointments() {
        // Arrange
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(appointmentRepository.findAllByUser(any(User.class))).thenReturn(List.of(new Appointment()));
        when(appointmentMapper.toDto(any(Appointment.class))).thenReturn(new AppointmentDTO());

        // Act
        List<AppointmentDTO> result = appointmentService.getAllAppointments(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * to TestSearchAppointment
     */
    @Test
    void testSearchAppointments() {
        // Arrange
        Appointment appointment = new Appointment();
        Page<Appointment> page = new PageImpl<>(List.of(appointment));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(appointmentRepository.searchAppointmentsByUser(anyString(), anyLong(), any(PageRequest.class)))
                .thenReturn(page);
        when(appointmentMapper.toDto(any(Appointment.class))).thenReturn(new AppointmentDTO());

        // Act
        Page<AppointmentDTO> result = appointmentService.searchAppointments("query", 0, 10, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    /**
     * To Test FilterAndSortAppointment
     */
    @Test
    void testFilterAndSortAppointments() {
        // Arrange
        Appointment appointment = new Appointment();
        Page<Appointment> page = new PageImpl<>(List.of(appointment));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(appointmentRepository.filterAndSortAppointmentsByUser(anyLong(), anyString(), anyString(),
                anyString(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class), anyLong()))
                .thenReturn(page);
        when(appointmentMapper.toDto(any(Appointment.class))).thenReturn(new AppointmentDTO());

        // Act
        Page<AppointmentDTO> result = appointmentService.filterAndSortAppointments(
                1L, "title", "description", "location", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 0, 10, Sort.by("startDateTime"), 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

}
