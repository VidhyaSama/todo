package com.microservice.todolist.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * User Entity.
 * @author vidhya.sama
 *
 */
@Entity
@Table(name = "user_Info")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

  @Id
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "user_password")
  private String password;

  @Column(name = "age")
  private Integer age;

  @CreationTimestamp
  private Instant createdAt;

  @UpdateTimestamp
  private Instant updatedAt;

  @Lob
  @Column(name = "image", length = Integer.MAX_VALUE)
  private byte[] image;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user",
      fetch = FetchType.LAZY)
  @Builder.Default
  private List<Task> tasks = new ArrayList<Task>();
}
