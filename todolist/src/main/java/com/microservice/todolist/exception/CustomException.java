package com.microservice.todolist.exception;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Centralized exception handling.
 * @author vidhya.sama
 *
 */
@ControllerAdvice
public class CustomException extends ResponseEntityExceptionHandler {

  /**
   * Exception to be thrown when user is not found in database.
   * @param ex  the exception
   * @param request the current request
   * @return error message
   */
  @ExceptionHandler(UserNotFoundException.class)
  public final ResponseEntity<Object> handleUserNotFoundException(
      final Exception ex, final WebRequest request) {
    ErrorResponse error = ErrorResponse.builder()
        .error(ex.getLocalizedMessage()).build();
    return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
  }

  /**
   * Exception to be thrown when user image is not found in database.
   * @param ex  the exception
   * @param request the current request
   * @return error message
   */
  @ExceptionHandler(ImageNotFoundException.class)
  public final ResponseEntity<Object> handleImageNotFoundException(
      final Exception ex, final WebRequest request) {
    ErrorResponse error = ErrorResponse.builder()
        .error(ex.getLocalizedMessage()).build();
    return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
  }

  /**
   * Exception to be thrown when login credentials
   * are not matched is not found in database.
   * @param ex  the exception
   * @param request the current request
   * @return error message
   */
  @ExceptionHandler(InvalidCredentialsException.class)
  public final ResponseEntity<Object> handleInvalidCredentialsException(
      final Exception ex, final WebRequest request) {
    ErrorResponse error = ErrorResponse.builder()
        .error(ex.getLocalizedMessage()).build();
    return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
  }

  /**
   * Exception to be thrown when validation on an argument
   *  annotated with {@code @Valid} fails.
   * @param ex  the exception
   * @param headers the headers to be written to the response
   * @param status  the selected response status
   * @param request the current request
   * @return error message
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    List<String> details = new ArrayList<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      details.add(error.getField() + " " + error.getDefaultMessage());
    }
    ErrorResponse error = ErrorResponse.builder()
        .error(ex.getObjectName() + " Validation Failed").details(details)
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

  }

  /**
   * Exception to be thrown when requested DML operation resulted in a
   * violation of a defined integrity constraint.
   * @param ex  the exception
   * @param request the current request
   * @return error message
   */
  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolationException(
      final ConstraintViolationException ex, final WebRequest request) {
    List<String> details = new ArrayList<>();
    details.add(ex.getConstraintName());
    details.add(ex.getMessage());
    details.add(ex.getCause().toString());
    ErrorResponse error = ErrorResponse.builder().details(details).build();
    return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);

  }

  /**
   * Exception to be thrown for any checked or unchecked exceptions.
   * @param ex  the exception
   * @param request the current request
   * @return error message
   */
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleExceptions(final Exception ex,
      final WebRequest request) {
    ErrorResponse error = ErrorResponse.builder()
        .error(ex.getLocalizedMessage()).build();
    return new ResponseEntity<Object>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
