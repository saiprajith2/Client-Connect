package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Appointment;
import com.sails.client_connect.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppointmentRepositoryTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentRepositoryTest appointmentRepositoryTest;


    /**
     * Test case for searching appointments by user.
     */
    @Test
    void testSearchAppointmentsByUser() {
        Appointment appointment = new Appointment();
        Page<Appointment> appointmentPage = new PageImpl<>(Collections.singletonList(appointment), PageRequest.of(0, 10), 1);

        when(appointmentRepository.searchAppointmentsByUser(anyString(), anyLong(), any(Pageable.class)))
                .thenReturn(appointmentPage);

        Page<Appointment> result = appointmentRepository.searchAppointmentsByUser("test", 1L, PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals(appointment, result.getContent().get(0));
    }

    /**
     * Test case for filtering and sorting appointments by user.
     */
    @Test
    void testFilterAndSortAppointmentsByUser() {
        Appointment appointment = new Appointment();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "startDateTime"));
        Page<Appointment> appointmentPage = new PageImpl<>(Arrays.asList(appointment), pageable, 1);

        when(appointmentRepository.filterAndSortAppointmentsByUser(
                anyLong(), anyString(), anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class), anyLong()))
                .thenReturn(appointmentPage);

        Page<Appointment> result = appointmentRepository.filterAndSortAppointmentsByUser(
                1L, "test title", "test description", "test location", LocalDateTime.now(), LocalDateTime.now().plusDays(1), pageable, 1L);

        assertEquals(1, result.getTotalElements());
        assertEquals(appointment, result.getContent().get(0));
    }

    /**
     * Test case for finding all appointments by user.
     */
    @Test
    void testFindAllByUser() {
        User user = new User();
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        when(appointmentRepository.findAllByUser(user)).thenReturn(Arrays.asList(appointment));

        Iterable<Appointment> result = appointmentRepository.findAllByUser(user);
        assertEquals(1, ((Collection<?>) result).size());
        assertEquals(appointment, ((Collection<?>) result).iterator().next());
    }
}
