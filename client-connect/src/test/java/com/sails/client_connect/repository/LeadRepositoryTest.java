package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Lead;
import com.sails.client_connect.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class LeadRepositoryTest {

    @Mock
    private LeadRepository leadRepository;

    @InjectMocks
    private LeadRepositoryTest leadRepositoryTest;

    /**
     * Test for finding a lead by ID and user.
     */
    @Test
    void testFindByIdAndUser() {
        User user = new User(); // Initialize user
        Lead lead = new Lead(); // Initialize lead
        lead.setId(1L);
        lead.setUser(user);

        // Assign
        when(leadRepository.findByIdAndUser(anyLong(), any(User.class)))
                .thenReturn(Optional.of(lead));

        // Act
        Optional<Lead> result = leadRepository.findByIdAndUser(1L, user);

        // Assert
        assertEquals(lead, result.orElse(null));
    }

    /**
     * Test for finding all leads by user.
     */
    @Test
    void testFindAllByUser() {
        User user = new User(); // Initialize user
        Lead lead1 = new Lead(); // Initialize lead
        Lead lead2 = new Lead(); // Initialize lead
        lead1.setUser(user);
        lead2.setUser(user);

        // Assign
        when(leadRepository.findAllByUser(any(User.class)))
                .thenReturn(Arrays.asList(lead1, lead2));

        // Act
        List<Lead> result = leadRepository.findAllByUser(user);

        // Assert
        assertEquals(2, result.size());
        assertEquals(lead1, result.get(0));
        assertEquals(lead2, result.get(1));
    }
}
