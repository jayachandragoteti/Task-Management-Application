package io.github.jayachandragoteti.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.jayachandragoteti.taskmanagement.models.Task;

public interface TaskRepository extends JpaRepository<Task,Long> {
    // Additional query methods can be added here if needed
} 