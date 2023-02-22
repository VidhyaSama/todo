package com.microservice.todolist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import com.microservice.todolist.dto.TaskDto;
import com.microservice.todolist.dto.TaskResponse;
import com.microservice.todolist.dto.TaskUpdate;
import com.microservice.todolist.dto.UserTask;
import com.microservice.todolist.entity.Task;
import com.microservice.todolist.entity.User;
import com.microservice.todolist.repository.TaskRepository;
import com.microservice.todolist.repository.UserRepository;


@SpringBootTest
public class TaskServiceImpTest {
  @InjectMocks
  private TaskServiceImp taskServiceImp;
  
  @Mock
  private UserRepository userRepository;

  @Mock
  private TaskRepository taskRepository;
 
  @Mock
  private UserService userService;
  
  private TaskDto taskDto;

  private TaskResponse taskResponse;

  private List<TaskDto> taskDtos = new ArrayList<TaskDto>();
  
  private List<Task> tasks = new ArrayList<Task>();

  private UserTask userTask;
  
  private Task task;
  
  private Optional<Task> optionalTask;
  
  private User user;
  
  String token = "eyJhbGciOiJIUzUxMiJ9."
      + "eyJzdWIiOiJ2aWRoeWFAZ21haWwuY29tIiwiZXhwIjoxNjQyNDM3MTQ2LCJpYXQiOjE2NDI0MTkxNDZ9."
      + "Bv5OMK2O4xHs5RhDh_x9EojT_vKxfrlsGHWzpEPznddF-N4PlVaCy8jeTDuxZxrJyGLd002cfqUlLSTw9Sotqg";

  @BeforeEach
  public void setUp() {
    taskDto = TaskDto.builder().description("Read book").completed(false)
        .id("61e66ec5d42e52001722fd24")
        .owner("61e0b02f2dc4ea00175a4b51").build();
    user = User.builder().id("Ga6JPebMFZXjiqF75LHcTG7L")
        .age(23).email("Vidhya@gmail.com").name("Sama Vidhya").build();
    task = Task.builder().description("Read book").completed(false)
        .id("61e66ec5d42e52001722fd24").user(user).build();
    tasks.add(task);
    taskResponse = TaskResponse.builder().data(taskDto).success(true).build();
    optionalTask = Optional.of(task);
    taskDtos.add(taskDto);
    userTask = UserTask.builder().count(1).data(taskDtos).build();
  }
  
  @Test
  public void addTaskTest()  {
    
    Mockito.when(userService.getUsername(any(String.class))).thenReturn(user.getEmail());
    Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(user);
    task.setId(RandomStringUtils.randomAlphanumeric(24));
    task.setUser(user);
    task.setDescription(taskDto.getDescription());
    Mockito.when(taskRepository.save(any())).thenReturn(task);
    BeanUtils.copyProperties(task, taskDto);
    taskDto.setOwner(task.getUser().getId());
    TaskResponse taskResponse = taskServiceImp.addTask("Bearer " + token, taskDto);
    assertTrue(taskResponse.getSuccess());
    
  }
  
  @Test
  public void getTasksTest()  {    
    Mockito.when(userService.getUsername(any(String.class))).thenReturn(user.getEmail());
    Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(user);
    Mockito.when(taskRepository.findByUser(any(User.class))).thenReturn(tasks);
    UserTask userTask = taskServiceImp.getTasks("Bearer " + token);
    assertEquals(1, userTask.getCount());   
  }
  
  @Test
  public void getTaskByIdTest() {
    
    Mockito.when(userService.getUsername(any(String.class))).thenReturn(user.getEmail());
    Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(user);
    Mockito.when(taskRepository.findByIdAndUser(any(String.class), any(User.class)))
        .thenReturn(optionalTask);
    BeanUtils.copyProperties(optionalTask.get(), taskDto);
    taskDto.setOwner(optionalTask.get().getUser().getId());
    TaskResponse taskResponse = taskServiceImp
        .getUserTaskById("Bearer " + token, "61e66ec5d42e52001722fd24"); 
    assertTrue(taskResponse.getSuccess());
  }
  
  
  @Test
  public void getTaskByIdNegativeTest() {    
    Mockito.when(userService.getUsername(any(String.class))).thenReturn(user.getEmail());
    Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(user);
    Mockito.when(taskRepository.findByIdAndUser(any(String.class), any(User.class)))
        .thenReturn(Optional.ofNullable(null));
    TaskResponse taskResponse = taskServiceImp
        .getUserTaskById("Bearer " + token, "61e66ec5d42e52001722fd24"); 
    assertFalse(taskResponse.getSuccess());
  }
  
  @Test
  public void getTaskByCompletedTest()  {   
    Mockito.when(userService.getUsername(any(String.class))).thenReturn(user.getEmail());
    Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(user);
    Mockito.when(taskRepository
        .findByUserAndCompleted(any(User.class), any(Boolean.class))).thenReturn(tasks);
    UserTask userTask = taskServiceImp.getUserTaskByCompleted("Bearer " + token, false);
    assertEquals(1, userTask.getCount());   
  }
  
  @Test
  public void getTaskByPagination() {   
    Mockito.when(userService.getUsername(any(String.class))).thenReturn(user.getEmail());
    Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(user);
    Mockito.when(taskRepository
        .findByUser(any(User.class), any(Integer.class), any(Integer.class)))
        .thenReturn(tasks);
    UserTask userTask = taskServiceImp.getTask("Bearer " + token, 1, 0);
    assertEquals(1, userTask.getCount());   
  }
  
  @Test
  public void deleteTaskTest() {
    
    Mockito.when(userService.getUsername(any(String.class))).thenReturn(user.getEmail());
    Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(user);
    Mockito.when(taskRepository.findByIdAndUser(any(String.class), any(User.class)))
        .thenReturn(optionalTask);
    doNothing().when(taskRepository).delete(any());
    TaskResponse taskResponse = taskServiceImp
        .deleteTask("Bearer " + token, "61e66ec5d42e52001722fd24"); 
    assertTrue(taskResponse.getSuccess());
  }
  
  @Test
  public void deleteTaskNegativeTest() throws Exception {
    
    Mockito.when(userService.getUsername(any(String.class))).thenReturn(user.getEmail());
    Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(user);
    Mockito.when(taskRepository.findByIdAndUser(any(String.class), any(User.class)))
        .thenReturn(Optional.ofNullable(null));
    TaskResponse taskResponse = taskServiceImp
        .deleteTask("Bearer " + token, "61e66ec5d42e52001722fd24"); 
    assertFalse(taskResponse.getSuccess());
  }

  @Test
  public void updateTaskTest() {
    TaskUpdate taskUpdate = new TaskUpdate();
    taskUpdate.setCompleted(Optional.of(true));    
    Mockito.when(userService.getUsername(any(String.class))).thenReturn(user.getEmail());
    Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(user);
    Mockito.when(taskRepository.findByIdAndUser(any(String.class), any(User.class)))
    .thenReturn(optionalTask);
    Mockito.when(taskRepository.save(any())).thenReturn(task);
    BeanUtils.copyProperties(task, taskDto);
    taskDto.setOwner(task.getUser().getId());
    TaskResponse taskResponse = taskServiceImp
        .updateTask("Bearer " + token, taskUpdate, "61e66ec5d42e52001722fd24" );
    assertTrue(taskResponse.getSuccess());  
  }
  
  @Test
  public void updateTaskNegativeTest() throws Exception {
    TaskUpdate taskUpdate = new TaskUpdate();
    taskUpdate.setCompleted(Optional.of(true));   
    Mockito.when(userService.getUsername(any(String.class))).thenReturn(user.getEmail());
    Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(user);
    Mockito.when(taskRepository.findByIdAndUser(any(String.class), any(User.class)))
        .thenReturn(Optional.ofNullable(null));
    TaskResponse taskResponse = taskServiceImp
        .updateTask("Bearer " + token, taskUpdate, "61e66ec5d42e52001722fd24" ); 
    assertFalse(taskResponse.getSuccess());
  }
}
