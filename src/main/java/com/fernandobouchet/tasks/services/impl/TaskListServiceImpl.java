package com.fernandobouchet.tasks.services.impl;

import com.fernandobouchet.tasks.domain.entities.TaskList;
import com.fernandobouchet.tasks.repositories.TaskListRepository;
import com.fernandobouchet.tasks.services.TaskListService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<TaskList> listTaskLists() {
        return taskListRepository.findAll();
    }

    @Transactional
    @Override
    public TaskList createTaskList(TaskList taskList) {
        if(taskList.getId() != null) {
            throw new IllegalArgumentException("Task list already has and ID!");
        }
        if(taskList.getTitle() == null || taskList.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task list title must be present");
        }

        LocalDateTime now = LocalDateTime.now();

        return taskListRepository.save(new TaskList(
                null,
                taskList.getTitle(),
                taskList.getDescription(),
                null,
                now,
                now
        ));
    }

    @Override
    public Optional<TaskList> getTaskList(UUID id) {
        return taskListRepository.findById(id);
    }

    @Transactional
    @Override
    public TaskList updateTaskList(UUID taskListId, TaskList taskList) {
      if(null == taskList.getId()) {
          throw new IllegalArgumentException("Task list must hava an ID");
      }

      if(!Objects.equals(taskList.getId(), taskListId)) {
          throw new IllegalArgumentException("Attempting to change task list ID, this is not permitted!");
      }

      TaskList existingTaskList = taskListRepository.findById(taskListId).orElseThrow(() -> new IllegalArgumentException("Task list not found"));

      existingTaskList.setTitle(taskList.getTitle());
      existingTaskList.setDescription(taskList.getDescription());
      existingTaskList.setUpdated(LocalDateTime.now());
      return taskListRepository.save(existingTaskList);
    }

    @Transactional
    @Override
    public void deleteTaskList(UUID taskListId) {
        taskListRepository.deleteById(taskListId);
    }
}
