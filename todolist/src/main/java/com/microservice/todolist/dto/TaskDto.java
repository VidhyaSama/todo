package com.microservice.todolist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * This used to store task information from client.
 * @author vidhya.sama
 *
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String id;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String owner;

  @NotBlank(message = "should  filled out")
  private String description;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Boolean completed;

  @CreationTimestamp
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Instant createdAt;

  @UpdateTimestamp
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Instant updatedAt;

}
