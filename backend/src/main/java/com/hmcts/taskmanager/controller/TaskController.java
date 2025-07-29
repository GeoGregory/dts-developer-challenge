package com.hmcts.taskmanager.controller;

import com.hmcts.taskmanager.model.Task;
import com.hmcts.taskmanager.repository.TaskRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody Task task) {
        return repository.save(task);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id, @RequestBody String status) {
        Optional<Task> taskOpt = repository.findById(id);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task task = taskOpt.get();
        task.setStatus(status);
        return ResponseEntity.ok(repository.save(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}