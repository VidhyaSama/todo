/***Service Layer **/
package com.microservice.todolist.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.todolist.dto.TaskDto;
import com.microservice.todolist.dto.TaskResponse;
import com.microservice.todolist.dto.TaskUpdate;
import com.microservice.todolist.dto.UserTask;
import com.microservice.todolist.entity.Task;
import com.microservice.todolist.entity.User;
import com.microservice.todolist.repository.TaskRepository;
import com.microservice.todolist.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * TaskServiceImp implements TaskService to fetch and add the user task to
 * database.<br>
 *
 * @author vidhya.sama
 *
 */
@Service
@Slf4j
public class TaskServiceImp implements TaskService {

  /**
   * Inject bean of type TaskRepository that perform crud operations on User
   * Table.
   *
   */
  @Autowired
  private UserRepository userRepository;

  /**
   * Inject bean of type TaskRepository that perform crud operations on task
   * Table.
   *
   */
  @Autowired
  private TaskRepository taskRepository;

  /**
   * Inject bean of type UserService to call its methods.
   *
   */
  @Autowired
  private UserService userService;

  /**
   * int identifier to specify the length of id.
   */
  public static final int ID_LENGTH = 24;

  /**
   * TaskDto identifier to copy values from task Entity.
   */
  private TaskDto taskResponse = new TaskDto();

  /**
   * This method is used to add the user task.<br>
   *
   * @param token   The token identifies the user the task to be added.
   * @param taskDto This contains task Details that has to be added.
   * @return a TaskResponse instance.
   */
  @Override
  public TaskResponse addTask(final String token, final TaskDto taskDto) {
    User user = userRepository.findByEmail(userService.getUsername(token));
    Task task = new Task();
    task.setId(RandomStringUtils.randomAlphanumeric(ID_LENGTH));
    task.setUser(user);
    task.setDescription(taskDto.getDescription());
    task = taskRepository.save(task);
    BeanUtils.copyProperties(task, taskDto);
    taskDto.setOwner(task.getUser().getId());
    return TaskResponse.builder().success(true).data(taskDto).build();
  }

  /**
   * Get all tasks taken up by user.
   *
   * @param token The token identifies the user and fetch the task taken up by
   *              user.
   * @return a UserTask instance.
   */
  @Override
  public UserTask getTasks(final String token) {
    User user = userRepository.findByEmail(userService.getUsername(token));
    List<Task> tasks = taskRepository.findByUser(user);
    List<TaskDto> taskDto = tasks.stream().map(data -> {
      TaskDto task = new TaskDto();
      BeanUtils.copyProperties(data, task);
      task.setOwner(data.getUser().getId());
      return task;
    }).collect(Collectors.toList());
    return UserTask.builder().count(taskDto.size()).data(taskDto).build();
  }

  /**
   * Get task by given taskId taken up by user .
   *
   * @param token  The token identifies the user.
   * @param taskId the taskId is to fetch particular task assigned to user.
   * @return a TaskResponse instance.
   */
  @Override
  public TaskResponse getUserTaskById(final String token, final String taskId) {
    User user = userRepository.findByEmail(userService.getUsername(token));
    Optional<Task> task = taskRepository.findByIdAndUser(taskId, user);
    if (!task.isPresent()) {
      return TaskResponse.builder().success(false).message("No task found")
          .build();
    }
    BeanUtils.copyProperties(task.get(), taskResponse);
    taskResponse.setOwner(task.get().getUser().getId());
    return TaskResponse.builder().success(true).data(taskResponse).build();
  }

  /**
   * Get task by its completed status taken up by user .
   *
   * @param token     The token identifies the user.
   * @param completed based on completed status value it fetches list of tasks
   *                  assigned to user.
   * @return a UserTask instance.
   */
  @Override
  public UserTask getUserTaskByCompleted(final String token,
      final Boolean completed) {
    User user = userRepository.findByEmail(userService.getUsername(token));
    List<Task> tasks = taskRepository.findByUserAndCompleted(user, completed);
    List<TaskDto> taskDto = tasks.stream().map(data -> {
      TaskDto task = new TaskDto();
      BeanUtils.copyProperties(data, task);
      task.setOwner(data.getUser().getId());
      return task;
    }).collect(Collectors.toList());
    return UserTask.builder().count(taskDto.size()).data(taskDto).build();
  }

  /**
   * Get task taken up by user by applying pagination.
   *
   * @param token The token identifies the user.
   * @param skip  that calculates the number of objects in the list to skip.
   * @param limit the number of objects that end up in the final List.
   * @return a  UserTask instance.
   */
  @Override
  public UserTask getTask(final String token, final Integer limit,
      final Integer skip) {
    User user = userRepository.findByEmail(userService.getUsername(token));
    List<Task> tasks = taskRepository.findByUser(user, limit, skip);
    List<TaskDto> taskDto = tasks.stream().map(data -> {
      TaskDto task = new TaskDto();
      BeanUtils.copyProperties(data, task);
      task.setOwner(data.getUser().getId());
      return task;
    }).collect(Collectors.toList());
    return UserTask.builder().count(taskDto.size()).data(taskDto).build();
  }

  /**
   * Update task details.
   *
   * @param token      The token identifies the user.
   * @param taskUpdate contains fields that to be updated.
   * @param taskId     the is used to identify the task that needs to be
   *                   updated.
   * @return a TaskResponse instance.
   */
  @Override
  public TaskResponse updateTask(final String token,
      final TaskUpdate taskUpdate, final String taskId) {
    User user = userRepository.findByEmail(userService.getUsername(token));
    Optional<Task> task = taskRepository.findByIdAndUser(taskId, user);
    if (!task.isPresent()) {
      return TaskResponse.builder().success(false).message("No task found")
          .build();
    }
    Task userTask = mapEntityToDto(taskUpdate, task.get());
    Task taskDetails = taskRepository.save(userTask);
    BeanUtils.copyProperties(taskDetails, taskResponse);
    taskResponse.setOwner(taskDetails.getUser().getId());
    return TaskResponse.builder().success(true).data(taskResponse).build();
  }

  // map Dto to entity
  private Task mapEntityToDto(final TaskUpdate taskUpdate, final Task task) {
    if (Optional.ofNullable(taskUpdate.getCompleted()).isPresent()) {
      task.setCompleted(taskUpdate.getCompleted().get());
    }

    if (Optional.ofNullable(taskUpdate.getDescription()).isPresent()) {
      task.setDescription(taskUpdate.getDescription().get());
    }
    return task;
  }

  /**
   * Delete task based on Id.
   *
   * @param token  The token identifies the user.
   * @param taskId the is used to identify the task that needs to be deleted.
   * @return a TaskResponse instance.
   */
  @Override
  public TaskResponse deleteTask(final String token, final String taskId) {
    User user = userRepository.findByEmail(userService.getUsername(token));
    Optional<Task> task = taskRepository.findByIdAndUser(taskId, user);
    if (!task.isPresent()) {
      return TaskResponse.builder().success(false).message("No task found")
          .build();
    }
    taskRepository.delete(task.get());
    return TaskResponse.builder().success(true).data(taskResponse).build();
  }

}
