
package com.microservice.todolist.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.todolist.config.JwtAuthenticationEntryPoint;
import com.microservice.todolist.dto.TaskDto;
import com.microservice.todolist.dto.TaskResponse;
import com.microservice.todolist.dto.TaskUpdate;
import com.microservice.todolist.dto.UserTask;
import com.microservice.todolist.service.TaskService;
import com.microservice.todolist.service.UserService;
import com.microservice.todolist.util.JwtTokenUtil;


@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @InjectMocks
  private TaskController taskController;

  @MockBean
  private TaskService taskService;

  @MockBean
  private UserService userService;

  @MockBean
  private JwtTokenUtil jwtTokenUtil;

  @MockBean
  private UserDetailsService userDetailsService;

  @MockBean
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  ObjectMapper mapper = new ObjectMapper();

  private TaskDto taskDto;

  private TaskResponse taskResponse;

  private List<TaskDto> tasks = new ArrayList<TaskDto>();

  private UserTask userTask;
  
  String token = "eyJhbGciOiJIUzUxMiJ9."
      + "eyJzdWIiOiJ2aWRoeWFAZ21haWwuY29tIiwiZXhwIjoxNjQyNDM3MTQ2LCJpYXQiOjE2NDI0MTkxNDZ9."
      + "Bv5OMK2O4xHs5RhDh_x9EojT_vKxfrlsGHWzpEPznddF-N4PlVaCy8jeTDuxZxrJyGLd002cfqUlLSTw9Sotqg";

  @BeforeEach
  public void setUp() {
    taskDto = TaskDto.builder().description("Read book").completed(false)
        .id("61e66ec5d42e52001722fd24")
        .owner("61e0b02f2dc4ea00175a4b51").build();
    taskResponse = TaskResponse.builder().data(taskDto).success(true).build();
    tasks.add(taskDto);
    userTask = UserTask.builder().count(1).data(tasks).build();
  }

  @Test
  public void addUserTaskTest() throws Exception {
    TaskDto task = new TaskDto();
    task.setDescription("Read book");
    String request = "{\"description\": \"Reading book\"}";   
    Mockito.when(taskService.addTask(any(String.class), any(TaskDto.class)))
        .thenReturn(taskResponse);
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.post("/task").content(request)
            .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token))
        .andReturn();
    assertEquals(201, result.getResponse().getStatus());
    Mockito.verify(taskService).addTask(any(String.class), any(TaskDto.class));
  }

  
  @Test
  public void updateUserTaskTest() throws Exception {
    String request = "{\"completed\": \"true\"}"; 
    taskDto.setCompleted(true);
    Mockito.when(taskService.updateTask(
        any(String.class), any(TaskUpdate.class), any(String.class) ))
        .thenReturn(taskResponse);
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.put("/task/{taskId}", "61e66ec5d42e52001722fd24")
            .content(request)
            .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token))
        .andReturn();
    String responseStr = result.getResponse().getContentAsString();
    mapper.disable(MapperFeature.USE_ANNOTATIONS);
    TaskResponse response = mapper.readValue(responseStr, TaskResponse.class);
    assertEquals(taskDto.getCompleted(), response.getData().getCompleted());
  }

  @Test
  public void getTaskByCompletedTest() throws Exception {
    Mockito.when(taskService.getUserTaskByCompleted(
        any(String.class), any(Boolean.class))).thenReturn(userTask);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .get("/task/completed").param("completed", "false")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)).andReturn();
    String responseStr = result.getResponse().getContentAsString();
    UserTask response = mapper.readValue(responseStr, UserTask.class);
    assertEquals(1, response.getCount());

  }
  
  @Test
  public void getTaskByIdTest() throws Exception {
    Mockito.when(taskService.getUserTaskById(
        any(String.class), any(String.class))).thenReturn(taskResponse);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .get("/task/{taskId}", "61e66ec5d42e52001722fd24")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)).andReturn();
    assertEquals(200, result.getResponse().getStatus());
  }
  
  @Test
  public void getTaskByIdNegativeTest() throws Exception {
    taskResponse = TaskResponse.builder().success(false).message("No task found").build();
    Mockito.when(taskService.getUserTaskById(
        any(String.class), any(String.class))).thenReturn(taskResponse);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .get("/task/{taskId}", "61e66ec5d42e52001722fd24")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)).andReturn();
    assertEquals(404, result.getResponse().getStatus());
  }
  
  @Test
  public void getAllTask() throws Exception {
    Mockito.when(taskService.getTasks(
        any(String.class))).thenReturn(userTask);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .get("/task")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)).andReturn();
    String responseStr = result.getResponse().getContentAsString();
    UserTask response = mapper.readValue(responseStr, UserTask.class);
    assertEquals(1, response.getCount());
  }
  
  @Test
  public void deleteUserTask() throws Exception {
    Mockito.when(taskService.deleteTask(
        any(String.class), any(String.class))).thenReturn(taskResponse);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .delete("/task/{taskId}", "61e66ec5d42e52001722fd24")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)).andReturn();
    String responseStr = result.getResponse().getContentAsString();
    TaskResponse response = mapper.readValue(responseStr, TaskResponse.class);
    assertEquals(true, response.getSuccess());
  }
  
  @Test
  public void deleteUserNegativeTask() throws Exception {
    taskResponse = TaskResponse.builder().success(false).message("No task found").build();
    Mockito.when(taskService.deleteTask(
        any(String.class), any(String.class))).thenReturn(taskResponse);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .delete("/task/{taskId}", "61e66ec5d42e52001722fd24")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)).andReturn();
    assertEquals(404, result.getResponse().getStatus());
  }
  
  @Test
  public void getTaskByPaginationNegativeTest() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .get("/task/pagination").param("limit", "-1").param("skip", "-2")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)).andReturn();
    assertEquals(500, result.getResponse().getStatus());
  }
  
  
}
