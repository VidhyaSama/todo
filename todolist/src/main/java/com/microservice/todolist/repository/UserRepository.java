package com.microservice.todolist.repository;

import com.microservice.todolist.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Crud Operations.
 * @author vidhya.sama
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

  /**
   * This to fetch the user based on email.
   * @param email
   * @return
   */
  User findByEmail(String email);

  /**
   * This to fetch the user based on email and Id.
   * @param userId
   * @param username
   * @return
   */
  Optional<User> findByIdAndEmail(String userId, String username);

}
