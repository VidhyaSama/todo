package com.microservice.todolist.dto;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This dto is to update task based on requirement.
 * @author vidhya.sama
 *
 */
public class TaskUpdate {
  private Optional<@NotBlank String> description;

  private Optional<@NotNull Boolean> completed;

  /** get description.*/
  public Optional<String> getDescription() {
    return description;
  }

  /** set description value.*/
  public void setDescription(final Optional<String> task) {
    this.description = task;
  }

  /** get completed status.*/
  public Optional<Boolean> getCompleted() {
    return completed;
  }

  /** set completed status.*/
  public void setCompleted(final Optional<Boolean> status) {
    this.completed = status;
  }

}
