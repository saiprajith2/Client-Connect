package com.sails.client_connect.service;

import com.sails.client_connect.dto.LeadDTO;
import com.sails.client_connect.entity.Lead;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.mapper.LeadMapper;
import com.sails.client_connect.repository.LeadRepository;
import com.sails.client_connect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LeadServiceTest {

    // Mocking the dependencies used in the LeadService class
    @Mock
    private LeadRepository leadRepository;

    @Mock
    private LeadMapper leadMapper;

    @Mock
    private UserRepository userRepository;

    // Injecting the mocks into the LeadService instance
    @InjectMocks
    private LeadService leadService;


    // Test case for creating a new Lead
    @Test
    void testCreateLead() {
        LeadDTO leadDTO = new LeadDTO();
        leadDTO.setUserId(1L); // Set user ID for the lead
        Lead lead = new Lead();
        User user = new User();
        Lead savedLead = new Lead();

        // Mocking the behavior of the dependencies
        when(leadMapper.toEntity(any(LeadDTO.class))).thenReturn(lead);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(leadRepository.save(any(Lead.class))).thenReturn(savedLead);
        when(leadMapper.toDto(any(Lead.class))).thenReturn(leadDTO);

        // Calling the service method to create a lead
        LeadDTO result = leadService.createLead(leadDTO);

        // Verifying the results and behavior
        assertNotNull(result);
        verify(leadRepository).save(any(Lead.class));
    }

    // Test case for retrieving a Lead by ID and User ID
    @Test
    void testGetLeadByIdAndUserId() {
        Lead lead = new Lead();
        LeadDTO leadDTO = new LeadDTO();
        User user = new User();

        // Mocking the behavior of the dependencies
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(leadRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(lead));
        when(leadMapper.toDto(any(Lead.class))).thenReturn(leadDTO);

        // Calling the service method to retrieve a lead by ID and User ID
        LeadDTO result = leadService.getLeadByIdAndUserId(1L, 1L);

        // Verifying the results and behavior
        assertNotNull(result);
        verify(leadRepository).findByIdAndUser(anyLong(), any(User.class));
    }

    // Test case for retrieving all leads associated with a specific User ID
    @Test
    void testGetAllLeadsByUserId() {
        User user = new User();
        List<Lead> leads = List.of(new Lead(), new Lead());
        List<LeadDTO> leadDTOs = List.of(new LeadDTO(), new LeadDTO());

        // Mocking the behavior of the dependencies
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(leadRepository.findAllByUser(any(User.class))).thenReturn(leads);
        when(leadMapper.toDto(any(Lead.class))).thenReturn(new LeadDTO());

        // Calling the service method to retrieve all leads by User ID
        List<LeadDTO> result = leadService.getAllLeadsByUserId(1L);

        // Verifying the results and behavior
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(leadRepository).findAllByUser(any(User.class));
    }

    // Test case for retrieving all leads in the system
    @Test
    void testGetAllLeads() {
        List<Lead> leads = List.of(new Lead(), new Lead());
        List<LeadDTO> leadDTOs = List.of(new LeadDTO(), new LeadDTO());

        // Mocking the behavior of the dependencies
        when(leadRepository.findAll()).thenReturn(leads);
        when(leadMapper.toDto(any(Lead.class))).thenReturn(new LeadDTO());

        // Calling the service method to retrieve all leads
        List<LeadDTO> result = leadService.getAllLeads();

        // Verifying the results and behavior
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(leadRepository).findAll();
    }

    // Test case for updating an existing lead
    @Test
    void testUpdateLead() {
        LeadDTO leadDTO = new LeadDTO();
        leadDTO.setFirstName("Updated Name"); // Update lead's first name
        Lead lead = new Lead();
        Lead updatedLead = new Lead();

        // Mocking the behavior of the dependencies
        when(leadRepository.findById(anyLong())).thenReturn(Optional.of(lead));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(leadRepository.save(any(Lead.class))).thenReturn(updatedLead);
        when(leadMapper.toDto(any(Lead.class))).thenReturn(leadDTO);

        // Calling the service method to update a lead
        LeadDTO result = leadService.updateLead(1L, leadDTO);

        // Verifying the results and behavior
        assertNotNull(result);
        assertEquals("Updated Name", result.getFirstName());
        verify(leadRepository).save(any(Lead.class));
    }

    // Test case for deleting a lead by its ID and associated User ID
    @Test
    void testDeleteLead() {
        User user = new User();
        Lead lead = new Lead();

        // Mocking the behavior of the dependencies
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(leadRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(lead));

        // Calling the service method to delete a lead
        leadService.deleteLead(1L, 1L);

        // Verifying that the lead repository's delete method was called
        verify(leadRepository).delete(any(Lead.class));
    }
}
