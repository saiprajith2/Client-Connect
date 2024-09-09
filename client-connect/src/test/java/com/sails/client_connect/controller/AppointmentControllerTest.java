package com.sails.client_connect.controller;

import com.sails.client_connect.dto.AppointmentDTO;
import com.sails.client_connect.response.ApiResponse;
import com.sails.client_connect.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AppointmentController appointmentController;


    /**
     * Test for creating an appointment.
     */
    @Test
    void testCreateAppointment() {
        when(session.getAttribute("userId")).thenReturn(1L);
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        when(appointmentService.createAppointment(any(AppointmentDTO.class), anyLong())).thenReturn(appointmentDTO);

        ResponseEntity<ApiResponse<AppointmentDTO>> response = appointmentController.createAppointment(appointmentDTO, session);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Appointment created successfully", response.getBody().getMessage());
        assertEquals(appointmentDTO, response.getBody().getData());
    }

    /**
     * Test for updating an appointment.
     */
    @Test
    void testUpdateAppointment() {
        when(session.getAttribute("userId")).thenReturn(1L);
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        when(appointmentService.updateAppointment(anyLong(), any(AppointmentDTO.class), anyLong())).thenReturn(appointmentDTO);

        ResponseEntity<ApiResponse<AppointmentDTO>> response = appointmentController.updateAppointment(1L, appointmentDTO, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Appointment updated successfully", response.getBody().getMessage());
        assertEquals(appointmentDTO, response.getBody().getData());
    }

    /**
     * Test for deleting an appointment.
     */
    @Test
    void testDeleteAppointment() {
        when(session.getAttribute("userId")).thenReturn(1L);

        ResponseEntity<ApiResponse<Void>> response = appointmentController.deleteAppointment(1L, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Appointment deleted successfully", response.getBody().getMessage());
    }

    /**
     * Test for retrieving an appointment by ID.
     */
    @Test
    void testGetAppointment() {
        when(session.getAttribute("userId")).thenReturn(1L);
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        when(appointmentService.getAppointment(anyLong(), anyLong())).thenReturn(appointmentDTO);

        ResponseEntity<ApiResponse<AppointmentDTO>> response = appointmentController.getAppointment(1L, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Appointment retrieved successfully", response.getBody().getMessage());
        assertEquals(appointmentDTO, response.getBody().getData());
    }

    /**
     * Test for retrieving all appointments.
     */
    @Test
    void testGetAllAppointments() {
        when(session.getAttribute("userId")).thenReturn(1L);
        List<AppointmentDTO> appointments = Collections.singletonList(new AppointmentDTO());
        when(appointmentService.getAllAppointments(anyLong())).thenReturn(appointments);

        ResponseEntity<ApiResponse<List<AppointmentDTO>>> response = appointmentController.getAllAppointments(session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Appointments retrieved successfully", response.getBody().getMessage());
        assertEquals(appointments, response.getBody().getData());
    }

    /**
     * Test for searching appointments based on a query.
     */
    @Test
    void testSearchAppointments() {
        when(session.getAttribute("userId")).thenReturn(1L);
        Page<AppointmentDTO> appointmentPage = new PageImpl<>(Collections.singletonList(new AppointmentDTO()));
        when(appointmentService.searchAppointments(anyString(), anyInt(), anyInt(), anyLong())).thenReturn(appointmentPage);

        ResponseEntity<Page<AppointmentDTO>> response = appointmentController.searchAppointments("query", 0, 10, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(appointmentPage, response.getBody());
    }

    /**
     * Test for filtering and sorting appointments.
     */
    @Test
    void testFilterAndSortAppointments() {
        Long userId = 1L;
        Long id = 2L;
        String title = "Meeting";
        String description = "Project meeting";
        String location = "Conference Room";
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(1);
        int page = 0;
        int size = 10;
        String sortBy = "startDateTime";
        String sortDir = "asc";
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        Page<AppointmentDTO> appointmentPage = new PageImpl<>(Collections.singletonList(appointmentDTO), PageRequest.of(page, size, sort), 1);

        when(session.getAttribute("userId")).thenReturn(userId);
        when(appointmentService.filterAndSortAppointments(
                eq(id), eq(title), eq(description), eq(location),
                eq(startDateTime), eq(endDateTime),
                eq(page), eq(size), eq(sort), eq(userId)))
                .thenReturn(appointmentPage);

        ResponseEntity<Page<AppointmentDTO>> response = appointmentController.filterAndSortAppointments(
                id, title, description, location,
                startDateTime, endDateTime, page, size, sortBy, sortDir, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(session).getAttribute("userId");
        verify(appointmentService).filterAndSortAppointments(
                eq(id), eq(title), eq(description), eq(location),
                eq(startDateTime), eq(endDateTime),
                eq(page), eq(size), eq(sort), eq(userId));
    }
}
