package com.microservice.todolist.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.microservice.todolist.dto.TaskDto;
import com.microservice.todolist.dto.TaskResponse;
import com.microservice.todolist.dto.TaskUpdate;
import com.microservice.todolist.dto.UserTask;
import com.microservice.todolist.service.TaskService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Task API.
 *
 * @author vidhya.sama
 *
 */
@RestController
@RequestMapping("/task")
@OpenAPIDefinition(info = @Info(title = "Task API"))
public class TaskController {

  @Autowired
  private TaskService taskService;

  /**
   * This method is used to add the user task.<br>
   *
   * @param token   The token identifies the user the task to be added.
   * @param taskDto This contains task Details that has to be added
   * @return a saved task record
   */
  @PostMapping
  @Operation(summary = "Add task")
  public ResponseEntity<?> addUserTask(
      @RequestHeader("Authorization") final String token,
      @Valid @RequestBody final  TaskDto taskDto) {
    TaskResponse task = taskService.addTask(token, taskDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(task);

  }

  /**
   * Get all tasks taken up by user.
   *
   * @param token The token identifies the user and fetch the task taken up by
   *              user.
   * @return taskCount and List of tasks.
   */
  @GetMapping
  @Operation(summary = "Get all User tasks")
  public ResponseEntity<?> getAllTask(
      @RequestHeader("Authorization") final String token) {
    UserTask task = taskService.getTasks(token);
    return ResponseEntity.status(HttpStatus.OK).body(task);
  }

  /**
   * Get task by given taskId taken up by user .
   *
   * @param token The token identifies the user.
   * @param taskId the taskId is to fetch particular task assigned to user.
   * @return true and taskDetails if found else return false.
   */
  @GetMapping("/{taskId}")
  @Operation(summary = "Get task from taskId")
  public ResponseEntity<?> getTaskById(
      @RequestHeader("Authorization") final String token,
      @PathVariable final String taskId) {
    TaskResponse task = taskService.getUserTaskById(token, taskId);
    if (!task.getSuccess()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(task);
    }
    return ResponseEntity.status(HttpStatus.OK).body(task);

  }

  /**
   * Get task by its completed status taken up by user .
   *
   * @param token The token identifies the user.
   * @param completed based on completed status value it fetches list of tasks
   *        assigned to user.
   * @return taskCount and List of tasks based on completed status.
   */
  @GetMapping("/completed")
  @Operation(summary = "Fetch tasks based on its completed status")
  public ResponseEntity<?> getTaskByCompleted(
      @RequestHeader("Authorization") final String token,
      @RequestParam(name = "completed") final Boolean completed) {
    UserTask task = taskService.getUserTaskByCompleted(token, completed);
    return ResponseEntity.status(HttpStatus.OK).body(task);

  }

  /**
   * Get task taken up by user by applying pagination.
   *
   * @param token The token identifies the user.
   * @param skip that calculates the number of objects in the list to skip.
   * @param limit the number of objects that end up in the final List.
   * @return taskCount and List of tasks.
   */
  @GetMapping("/pagination")
  @Operation(summary = "Get task by applying pagination")
  public ResponseEntity<?> getTaskByPagination(
      @RequestHeader("Authorization") final String token,
      @RequestParam final Integer limit, @RequestParam final Integer skip) {
    if (limit < 0 || skip < 0) {
      throw new IllegalArgumentException("skip or limit should not be < 0");
    }
    UserTask task = taskService.getTask(token, limit, skip);
    return ResponseEntity.status(HttpStatus.OK).body(task);

  }

  /**
   * Update task details.
   *
   * @param token      The token identifies the user.
   * @param taskUpdate contains fields that to be updated.
   * @param taskId the is used to identify the task that needs to be updated
   * @return true and task if task present else return false with "No task
   *         found" message
   */
  @PutMapping("/{taskId}")
  @Operation(summary = "Update task based on its taskId ")
  public ResponseEntity<?> updateUserTask(
      @RequestHeader("Authorization") final String token,
      @Valid @RequestBody final TaskUpdate taskUpdate,
      @PathVariable final String taskId)
      throws JsonMappingException {
    TaskResponse task = taskService.updateTask(token, taskUpdate, taskId);
    return ResponseEntity.status(HttpStatus.OK).body(task);

  }

  /**
   * Delete task based on Id.
   *
   * @param token The token identifies the user.
   * @param taskId the is used to identify the task that needs to be deleted.
   * @return true and task with no content if present else return false with "No
   *         task found" message
   */
  @DeleteMapping("/{taskId}")
  @Operation(summary = "Delete task with given taskId")
  public ResponseEntity<?> deleteUserTask(
      @RequestHeader("Authorization") final String token,
      @PathVariable final String taskId) {
    TaskResponse task = taskService.deleteTask(token, taskId);
    if (!task.getSuccess()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(task);
    }
    return ResponseEntity.status(HttpStatus.OK).body(task);

  }

}
