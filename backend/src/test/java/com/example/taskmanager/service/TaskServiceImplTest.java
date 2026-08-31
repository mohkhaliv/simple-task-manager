package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task();
        sampleTask.setId(1L);
        sampleTask.setTitle("Buy groceries");
        sampleTask.setDescription("Milk, eggs, bread");
        sampleTask.setDone(false);
    }

    @Test
    void createTask_shouldSaveAndReturnTask_whenRequestIsValid() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Buy groceries");
        request.setDescription("Milk, eggs, bread");
        request.setDone(false);

        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        TaskResponse result = taskService.createTask(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Buy groceries");
        assertThat(result.isDone()).isFalse();

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getTaskById_shouldReturnTask_whenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        TaskResponse result = taskService.getTaskById(1L);

        assertThat(result.getTitle()).isEqualTo("Buy groceries");
    }

    @Test
    void getTaskById_shouldThrowException_whenTaskDoesNotExist() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(99L);
        });
    }

    @Test
    void toggleTaskStatus_shouldFlipDoneFlag() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse result = taskService.toggleTaskStatus(1L);

        assertThat(result.isDone()).isTrue();
    }

    @Test
    void deleteTask_shouldCallRepositoryDelete_whenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).delete(sampleTask);
    }
}