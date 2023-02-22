package com.microservice.todolist.exception;



/**
 * Exception to be thrown when user is not found in database.
 * @author vidhya.sama
 *
 */
@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {

  /**
   * Instance having detailed message.
   *
   * @param message message the detail message.
   */
  public UserNotFoundException(final String message) {
    super(message);

  }

}
