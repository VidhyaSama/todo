package com.microservice.todolist.repository;

import com.microservice.todolist.entity.Task;
import com.microservice.todolist.entity.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Crud Opertaions.
 * @author vidhya.sama
 *
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

  /**
   * This is used to fetch tasks taken up by the user from the database.
   * @param user
   * @return tasks
   */
  List<Task> findByUser(User user);

  /**
   * This is used to fetch task based on taskId
   *  taken up by the user from the database.
   * @param taskId
   * @param user
   * @return {@Code Task}
   */
  Optional<Task> findByIdAndUser(String taskId, User user);

  /**
   * This is used to fetch task based on completed status
   *  taken up by the user from the database.
   * @param user
   * @param completed
   * @return tasks
   */
  List<Task> findByUserAndCompleted(User user, Boolean completed);

  /**
   * This is used to fetch task based on limit and offset.
   *
   * @param user
   * @param limit
   * @param offset
   * @return tasks
   */
  @Query(value = "select t.* from task t where t.owner = ?1 "
      + "order by created_at limit ?2 offset ?3 ", nativeQuery = true)
  List<Task> findByUser(User user, Integer limit, Integer skip);

}
