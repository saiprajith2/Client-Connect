package com.sails.client_connect.controller;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.response.ApiResponse;
import com.sails.client_connect.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private TaskController taskController;


    /**
     * Tests that getAllTasks() returns a list of tasks when the user is authorized.
     */
    @Test
    void testGetAllTasks() {
        when(session.getAttribute("userId")).thenReturn(1L);
        when(taskService.getAllTasks(anyLong(), any(), any(), any()))
                .thenReturn(Collections.singletonList(new TaskDTO()));

        ResponseEntity<ApiResponse<List<TaskDTO>>> response = taskController.getAllTasks(session, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getData());
    }

    /**
     * Tests that getTaskById() returns a task when the user is authorized and the task exists.
     */
    @Test
    void testGetTaskById_UserAuthorized() throws UserNotFoundException {
        when(session.getAttribute("userId")).thenReturn(1L);
        TaskDTO taskDTO = new TaskDTO();
        when(taskService.getTaskById(anyLong(), anyLong())).thenReturn(taskDTO);

        ResponseEntity<ApiResponse<TaskDTO>> response = taskController.getTaskById(1L, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getData());
    }

    /**
     * Tests that getTaskById() returns an UNAUTHORIZED response when the user is not authorized.
     */
    @Test
    void testGetTaskById_UserUnauthorized() {
        when(session.getAttribute("userId")).thenReturn(null);

        ResponseEntity<ApiResponse<TaskDTO>> response = taskController.getTaskById(1L, session);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Tests that getTaskById() returns a NOT_FOUND response when the task is not found.
     */
    @Test
    void testGetTaskById_TaskNotFound() throws UserNotFoundException {
        when(session.getAttribute("userId")).thenReturn(1L);
        when(taskService.getTaskById(anyLong(), anyLong())).thenThrow(new UserNotFoundException("Task not found"));

        ResponseEntity<ApiResponse<TaskDTO>> response = taskController.getTaskById(1L, session);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests that createTask() returns a CREATED response when the user is authorized.
     */
    @Test
    void testCreateTask_UserAuthorized() {
        when(session.getAttribute("userId")).thenReturn(1L);
        TaskDTO taskDTO = new TaskDTO();
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(taskDTO);

        ResponseEntity<ApiResponse<TaskDTO>> response = taskController.createTask(taskDTO, session);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getData());
    }

    /**
     * Tests that createTask() returns an UNAUTHORIZED response when the user is not authorized.
     */
    @Test
    void testCreateTask_UserUnauthorized() {
        when(session.getAttribute("userId")).thenReturn(null);

        ResponseEntity<ApiResponse<TaskDTO>> response = taskController.createTask(new TaskDTO(), session);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Tests that patchUpdateTask() returns an OK response when the user is authorized.
     */
    @Test
    void testPatchUpdateTask_UserAuthorized() {
        when(session.getAttribute("userId")).thenReturn(1L);
        TaskDTO taskDTO = new TaskDTO();
        when(taskService.patchUpdateTask(anyLong(), any(TaskDTO.class))).thenReturn(taskDTO);

        ResponseEntity<ApiResponse<TaskDTO>> response = taskController.patchUpdateTask(1L, taskDTO, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getData());
    }

    /**
     * Tests that patchUpdateTask() returns an UNAUTHORIZED response when the user is not authorized.
     */
    @Test
    void testPatchUpdateTask_UserUnauthorized() {
        when(session.getAttribute("userId")).thenReturn(null);

        ResponseEntity<ApiResponse<TaskDTO>> response = taskController.patchUpdateTask(1L, new TaskDTO(), session);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Tests that deleteTask() returns an OK response when the user is authorized.
     */
    @Test
    void testDeleteTask_UserAuthorized() throws UserNotFoundException {
        when(session.getAttribute("userId")).thenReturn(1L);

        ResponseEntity<ApiResponse<String>> response = taskController.deleteTask(1L, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Tests that deleteTask() returns an UNAUTHORIZED response when the user is not authorized.
     */
    @Test
    void testDeleteTask_UserUnauthorized() {
        when(session.getAttribute("userId")).thenReturn(null);

        ResponseEntity<ApiResponse<String>> response = taskController.deleteTask(1L, session);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Tests that searchTasks() returns a paginated list of tasks based on the search query.
     */
    @Test
    void testSearchTasks() {
        when(session.getAttribute("userId")).thenReturn(1L);
        Page<TaskDTO> page = new PageImpl<>(Collections.singletonList(new TaskDTO()), PageRequest.of(0, 10), 1);
        when(taskService.searchTasks(any(), anyInt(), anyInt(), anyLong())).thenReturn(page);

        ResponseEntity<Page<TaskDTO>> response = taskController.searchTasks("query", 0, 10, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Tests that filterAndSortTasks() returns a paginated list of tasks based on the filter and sorting criteria.
     */
    @Test
    void testFilterAndSortTasks() {
        when(session.getAttribute("userId")).thenReturn(1L);
        Page<TaskDTO> page = new PageImpl<>(Collections.singletonList(new TaskDTO()), PageRequest.of(0, 10), 1);
        when(taskService.filterAndSortTasks(any(), any(), any(), any(), any(), any(), anyInt(), anyInt(), any(), anyLong()))
                .thenReturn(page);

        ResponseEntity<Page<TaskDTO>> response = taskController.filterAndSortTasks(
                1L, "clientName", "taskTitle", "status", "priority", LocalDateTime.now(), 0, 10, "taskTitle", "asc", session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
