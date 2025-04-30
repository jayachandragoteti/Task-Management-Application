package io.github.jayachandragoteti.taskmanagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.jayachandragoteti.taskmanagement.models.Task;
import io.github.jayachandragoteti.taskmanagement.repository.TaskRepository;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;

    
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails){
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task newTaskDetails = new Task();
            newTaskDetails.setTitle(taskDetails.getTitle());
            newTaskDetails.setDescription(taskDetails.getDescription());
            newTaskDetails.setIsCompleted(taskDetails.getIsCompleted());
            return taskRepository.save(newTaskDetails);
        } else {
            return null;
        }
    }
    
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
}
