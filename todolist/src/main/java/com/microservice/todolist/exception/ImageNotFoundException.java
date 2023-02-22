package com.microservice.todolist.exception;


/**
 * Exception to be thrown when user image is not found in database.
 * @author vidhya.sama
 *
 */
@SuppressWarnings("serial")
public class ImageNotFoundException extends RuntimeException {

  /**
   * Instance having detailed message.
   *
   * @param message the detail message.
   */
  public ImageNotFoundException(final String message) {
    super(message);

  }
}
