package com.microservice.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Optional;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * This dto is to update user details based on requirement.
 * @author vidhya.sama
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

  private Optional<@NotEmpty @Pattern(
      regexp = "(^([A-Z][a-z]*((\\s)))+[A-Z][a-z]*$)",
      message = "should in give pattern "
          + "eg:(Firstname Lastname)") String> name;

  private Optional<@NotEmpty @Email String> email;

  private Optional<@NotEmpty @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])"
      + "(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$",
      message = "should contain 8-15 characters having atleast one "
          + "(lowercase,uppercase "
          + "letters,digits,special characters)") String> password;
  private Optional<@NotNull @Min(value = 1) Integer> age;

  /** get name.*/
  public Optional<String> getName() {
    return name;
  }

  /** set name.*/
  public void setName(final Optional<String> userName) {
    this.name = userName;
  }

  /** get email.*/
  public Optional<String> getEmail() {
    return email;
  }

  /** set email.*/
  public void setEmail(final Optional<String> userEmail) {
    this.email = userEmail;
  }

  /** get password.*/
  public Optional<String> getPassword() {
    return password;
  }

  /**set password.*/
  public void setPassword(final Optional<String> userPassword) {
    this.password = userPassword;
  }

  /** get age.*/
  public Optional<Integer> getAge() {
    return age;
  }

  /** set age.*/
  public void setAge(final Optional<Integer> userAge) {
    this.age = userAge;
  }

}
