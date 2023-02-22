package com.microservice.todolist.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Task Entity.
 * @author vidhya.sama
 *
 */
@Entity
@Table(name = "Task")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
  @Id
  private String id;

  @Column(name = "description")
  private String description;

  @Column(name = "completed")
  @Builder.Default
  private Boolean completed = false;

  @CreationTimestamp
  private Instant createdAt;

  @UpdateTimestamp
  private Instant updatedAt;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "owner")
  private User user;
}
