/**
 * This is to implement todos application.<br>
 *
 * @author vidhya.sama
 *
 */
package com.microservice.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


/**
 * This is to implement todos application.<br>
 *
 * @author vidhya.sama
 *
 */
@OpenAPIDefinition(info = @Info(title = "TODOS Application", version = "v1"))
@SpringBootApplication
public class TodolistApplication {

  /**
   * main() method that starts up the Spring ApplicationContext.
   *
   * @param args It stores command line arguments
   */
  public static void main(final String[] args) {
    SpringApplication.run(TodolistApplication.class, args);
  }

  /**
   * This bean is used to encode the password.
   *
   * @return password object.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
