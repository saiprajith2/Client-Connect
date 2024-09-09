package com.sails.client_connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sails.client_connect.dto.LeadDTO;
import com.sails.client_connect.service.LeadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class LeadControllerTest {

    // MockMvc for simulating HTTP requests
    private MockMvc mockMvc;

    // Mocking the LeadService
    @Mock
    private LeadService leadService;

    // Injecting mocks into LeadController
    @InjectMocks
    private LeadController leadController;

    // ObjectMapper for JSON conversion
    private ObjectMapper objectMapper;

    // Set up before each test
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(leadController).build();
        objectMapper = new ObjectMapper();
    }

    // Test for retrieving a lead by its ID
    @Test
    void getLeadById() throws Exception {
        LeadDTO leadDTO = new LeadDTO(1L, "John", "Doe", "Male", "john.doe@example.com", "1234567890", 1L);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1L);

        when(leadService.getLeadByIdAndUserId(anyLong(), anyLong())).thenReturn(leadDTO);

        mockMvc.perform(get("/user/leads/{id}", 1L)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(leadDTO)));
    }

    // Test for retrieving all leads for a user
    @Test
    void getAllLeads() throws Exception {
        LeadDTO lead1 = new LeadDTO(1L, "John", "Doe", "Male", "john.doe@example.com", "1234567890", 1L);
        LeadDTO lead2 = new LeadDTO(2L, "Jane", "Doe", "Female", "jane.doe@example.com", "0987654321", 1L);
        List<LeadDTO> leads = Arrays.asList(lead1, lead2);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1L);

        when(leadService.getAllLeadsByUserId(anyLong())).thenReturn(leads);

        mockMvc.perform(get("/user/leads")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(leads)));
    }
}
