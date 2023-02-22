package com.microservice.todolist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * This is to store User Information from Client.
 * @author vidhya.sama
 *
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String id;

  @NotBlank(message = "should be filled out")
  @Pattern(regexp = "(^([A-Z][a-z]*((\\s)))+[A-Z][a-z]*$)",
  message = "should in give pattern eg:(Firstname Lastname)")
  private String name;

  @Email(message = "Please provide a valid email address")
  @NotBlank(message = "should be filled out")
  private String email;

  @NotBlank(message = "should be filled out")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])"
      + "(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$",
  message = "should contain 8-15 characters having atleast one "
      + "(lowercase,uppercase letters,digits,special characters)")
  private String password;

  @NotNull
  @Min(value = 1, message = "should be greater than 1")
  private Integer age;

  @CreationTimestamp
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Instant createdAt;

  @UpdateTimestamp
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Instant updatedAt;

}
