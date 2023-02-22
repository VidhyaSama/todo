package com.microservice.todolist.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.microservice.todolist.dto.TaskDto;
import com.microservice.todolist.dto.TaskResponse;
import com.microservice.todolist.dto.TaskUpdate;
import com.microservice.todolist.dto.UserTask;

public interface TaskService {

  /**
   * This method is used to add the user task.<br>
   *
   * @param token   The token identifies the user the task to be added.
   * @param taskDto This contains task Details that has to be added.
   * @return a {@Code TaskResponse} instance.
   */
  TaskResponse addTask(String token, TaskDto taskDto);

  /**
   * Get all tasks taken up by user.
   *
   * @param token The token identifies the user and fetch the task taken up by
   *              user.
   * @return a {@Code UserTask} instance.
   */
  UserTask getTasks(String token);

  /**
   * Get task by given taskId taken up by user .
   *
   * @param token The token identifies the user.
   * @param taskId the taskId is to fetch particular task assigned to user.
   * @return a {@Code TaskResponse} instance.
   */
  TaskResponse getUserTaskById(String token, String taskId);

  /**
   * Get task by its completed status taken up by user .
   *
   * @param token The token identifies the user.
   * @param completed based on completed status value it fetches list of tasks
   *        assigned to user.
   * @return a {@Code UserTask} instance.
   */
  UserTask getUserTaskByCompleted(String token, Boolean completed);

  /**
   * Get task taken up by user by applying pagination.
   *
   * @param token The token identifies the user.
   * @param skip that calculates the number of objects in the list to skip.
   * @param limit the number of objects that end up in the final List.
   * @return a {@Code UserTask} instance.
   */
  UserTask getTask(String token, Integer limit, Integer skip);

  /**
   * Update task details.
   *
   * @param token      The token identifies the user.
   * @param taskUpdate contains fields that to be updated.
   * @param taskId the is used to identify the task that needs to be updated.
   * @return a {@Code TaskResponse} instance.
   */
  TaskResponse updateTask(String token, TaskUpdate taskUpdate, String taskId)
      throws JsonMappingException;

  /**
   * Delete task based on Id.
   *
   * @param token The token identifies the user.
   * @param taskId the taskId is used to identify the task that needs to be
   *        deleted.
   * @return a {@Code TaskResponse} instance.
   */
  TaskResponse deleteTask(String token, String taskId);

}
