package com.hmcts.taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmcts.taskmanager.model.Task;
import com.hmcts.taskmanager.repository.TaskRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    void createTask() throws Exception {
        com.hmcts.taskmanager.model.Task task = new Task(
                "Test Task",
                "Sample description",
                "pending",
                LocalDateTime.now().plusDays(1));
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void getAllTasks() throws Exception {
        taskRepository.save(new Task(
                "One",
                "",
                "pending",
                LocalDateTime.now().plusDays(1)));
        taskRepository.save(new Task(
                "Two",
                "",
                "done",
                LocalDateTime.now().plusDays(2)));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getTaskById() throws Exception {
        Task task = taskRepository.save(new Task(
                "FindMe",
                "",
                "pending",
                LocalDateTime.now().plusDays(1)));

        mockMvc.perform(get("/api/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("FindMe"));
    }

    @Test
    void updateTaskStatus() throws Exception {
        Task task = taskRepository.save(new Task(
                "Updatable",
                "",
                "pending",
                LocalDateTime.now().plusDays(1)));

        mockMvc.perform(put("/api/tasks/" + task.getId() + "/status")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("done"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks/" + task.getId()))
                .andExpect(jsonPath("$.status").value("done"));
    }

    @Test
    void deleteTask() throws Exception {
        Task task = taskRepository.save(new Task(
                "ToDelete",
                "",
                "pending",
                LocalDateTime.now().plusDays(1)));

        mockMvc.perform(delete("/api/tasks/" + task.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks/" + task.getId()))
                .andExpect(status().isNotFound());
    }
}
