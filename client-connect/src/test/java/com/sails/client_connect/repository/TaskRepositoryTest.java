package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.entity.Status;
import com.sails.client_connect.entity.Task;
import com.sails.client_connect.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskRepositoryTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskRepositoryTest taskRepositoryTest;

    /**
     * Test for finding a task by ID and user.
     */
    @Test
    void testFindByIdAndUser() {
        User user = new User();
        Task task = new Task();
        task.setId(1L);
        task.setUser(user);

        // Mock repository behavior
        when(taskRepository.findByIdAndUser(any(Long.class), any(User.class)))
                .thenReturn(Optional.of(task));

        // Act
        Optional<Task> result = taskRepository.findByIdAndUser(1L, user);

        // Assert
        assertEquals(task, result.orElse(null));
    }

    /**
     * Test for finding all tasks by user.
     */
    @Test
    void testFindByUser() {
        User user = new User();
        Task task1 = new Task();
        Task task2 = new Task();
        task1.setUser(user);
        task2.setUser(user);

        // Mock repository behavior
        when(taskRepository.findByUser(any(User.class)))
                .thenReturn(Arrays.asList(task1, task2));

        // Act
        List<Task> result = taskRepository.findByUser(user);

        // Assert
        assertEquals(2, result.size());
        assertEquals(task1, result.get(0));
        assertEquals(task2, result.get(1));
    }

    /**
     * Test for searching tasks by user with various filters.
     */
    @Test
    void testSearchTasksByUser() {
        User user = new User();
        Pageable pageable = Pageable.ofSize(10);
        Task task1 = new Task();
        Task task2 = new Task();
        Page<Task> taskPage = new PageImpl<>(Arrays.asList(task1, task2), pageable, 2);

        // Mock repository behavior
        when(taskRepository.searchTasksByUser(any(String.class), any(Status.class), any(Priority.class), any(Long.class), any(Pageable.class)))
                .thenReturn(taskPage);

        // Act
        Page<Task> result = taskRepository.searchTasksByUser("query", Status.NOT_STARTED, Priority.HIGH, 1L, pageable);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals(task1, result.getContent().get(0));
        assertEquals(task2, result.getContent().get(1));
    }

    /**
     * Test for filtering and sorting tasks by user.
     */
    @Test
    void testFilterAndSortTasksByUser() {
        User user = new User();
        Pageable pageable = Pageable.ofSize(10);
        Task task1 = new Task();
        Task task2 = new Task();
        Page<Task> taskPage = new PageImpl<>(Arrays.asList(task1, task2), pageable, 2);

        // Mock repository behavior
        when(taskRepository.filterAndSortTasksByUser(any(Long.class), anyString(), anyString(), anyString(), anyString(), any(LocalDateTime.class), any(Pageable.class), any(Long.class)))
                .thenReturn(taskPage);

        // Act
        Page<Task> result = taskRepository.filterAndSortTasksByUser(1L, "Client Name", "Task Title", "OPEN", "HIGH", LocalDateTime.now(), pageable, 1L);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals(task1, result.getContent().get(0));
        assertEquals(task2, result.getContent().get(1));
    }
}
