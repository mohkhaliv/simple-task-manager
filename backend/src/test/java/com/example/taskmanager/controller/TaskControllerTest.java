package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private TaskResponse sampleResponse;

    private TaskResponse buildSampleResponse() {
        return new TaskResponse(
                1L,
                "Buy groceries",
                "Milk, eggs, bread",
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void getAllTasks_shouldReturnListOfTasks() throws Exception {
        sampleResponse = buildSampleResponse();
        when(taskService.getAllTasks(null, null)).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Buy groceries"));
    }

    @Test
    void getTaskById_shouldReturnTask_whenTaskExists() throws Exception {
        sampleResponse = buildSampleResponse();
        when(taskService.getTaskById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Buy groceries"));
    }

    @Test
    void getTaskById_shouldReturn404_whenTaskDoesNotExist() throws Exception {
        when(taskService.getTaskById(99L)).thenThrow(new ResourceNotFoundException("Task not found"));

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask_shouldReturnCreatedTask_whenRequestIsValid() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Buy groceries");
        request.setDescription("Milk, eggs, bread");
        request.setDone(false);

        sampleResponse = buildSampleResponse();
        when(taskService.createTask(any(TaskRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Buy groceries"));
    }

    @Test
    void createTask_shouldReturn400_whenTitleIsBlank() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("");
        request.setDescription("Missing title");

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any(TaskRequest.class));
    }

    @Test
    void toggleTaskStatus_shouldReturnUpdatedTask() throws Exception {
        sampleResponse = buildSampleResponse();
        sampleResponse.setDone(true);
        when(taskService.toggleTaskStatus(1L)).thenReturn(sampleResponse);

        mockMvc.perform(patch("/api/tasks/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void deleteTask_shouldReturn200_whenTaskExists() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask(1L);
    }
}