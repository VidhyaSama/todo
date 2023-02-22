package com.microservice.todolist.exception;

/**
 * Exception to be thrown when login credentials
 * are not matched is not found in database.
 * @author vidhya.sama
 *
 */
@SuppressWarnings("serial")
public class InvalidCredentialsException extends RuntimeException {

  /**
   * Instance having detailed message.
   *
   * @param message message the detail message.
   */
  public InvalidCredentialsException(final String message) {
    super(message);

  }
}
