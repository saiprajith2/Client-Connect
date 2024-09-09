package com.sails.client_connect.service;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.entity.Task;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.mapper.TaskMapper;
import com.sails.client_connect.repository.CustomerRepository;
import com.sails.client_connect.repository.TaskRepository;
import com.sails.client_connect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    /**
     * To Get All the task associated with ID
     */
    @Test
    void getTaskById() {
        // Arrange
        Long taskId = 1L;
        Long userId = 1L;
        Task task = new Task();
        TaskDTO taskDTO = new TaskDTO();
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUser(taskId, user)).thenReturn(Optional.of(task));
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);

        // Act
        TaskDTO result = taskService.getTaskById(taskId, userId);

        // Assert
        assertNotNull(result);
        verify(taskRepository).findByIdAndUser(taskId, user);
    }

    /**
     * To test The method create Task
     */

    @Test
    void createTask() {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setUserId(1L);
        taskDTO.setCustomerId(1L);
        Task task = new Task();
        Task savedTask = new Task();
        TaskDTO savedTaskDTO = new TaskDTO();
        User user = new User();
        Customer customer = new Customer();

        when(userRepository.findById(taskDTO.getUserId())).thenReturn(Optional.of(user));
        when(customerRepository.findById(taskDTO.getCustomerId())).thenReturn(Optional.of(customer));
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(savedTask);
        when(taskMapper.toDTO(savedTask)).thenReturn(savedTaskDTO);

        // Act
        TaskDTO result = taskService.createTask(taskDTO);

        // Assert
        assertNotNull(result);
        verify(taskRepository).save(task);
    }

    /**
     * To test the method delete task
     */
    @Test
    void deleteTask() {
        // Arrange
        Long taskId = 1L;
        Long userId = 1L;
        Task task = new Task();
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUser(taskId, user)).thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(taskId, userId);

        // Assert
        verify(taskRepository).delete(task);
    }

    /*
    To Test Search task
     */
    @Test
    void searchTasks() {
        // Arrange
        String query = "HIGH";
        int page = 0;
        int size = 10;
        Long userId = 1L;
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = new PageImpl<>(Collections.emptyList());
        Page<TaskDTO> taskDTOPage = new PageImpl<>(Collections.emptyList());
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.searchTasksByUser(query, null, Priority.HIGH, userId, pageable)).thenReturn(taskPage);
        when(taskMapper.toDTO(any(Task.class))).thenReturn(new TaskDTO());

        // Act
        Page<TaskDTO> result = taskService.searchTasks(query, page, size, userId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getNumberOfElements());
    }

    /*
    To Test Filter and sortTask
     */
    @Test
    void filterAndSortTasks() {
        // Arrange
        Long id = 1L;
        String clientName = "ClientA";
        String taskTitle = "TitleA";
        String status = "IN_PROGRESS";
        String priority = "HIGH";
        LocalDateTime dueDateTime = LocalDateTime.now();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(Sort.Order.asc("dueDateTime"));
        Long userId = 1L;
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Task> taskPage = new PageImpl<>(Collections.emptyList());
        Page<TaskDTO> taskDTOPage = new PageImpl<>(Collections.emptyList());
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.filterAndSortTasksByUser(id, clientName, taskTitle, status, priority, dueDateTime, pageable, userId)).thenReturn(taskPage);
        when(taskMapper.toDTO(any(Task.class))).thenReturn(new TaskDTO());

        // Act
        Page<TaskDTO> result = taskService.filterAndSortTasks(id, clientName, taskTitle, status, priority, dueDateTime, page, size, sort, userId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getNumberOfElements());
    }
}
