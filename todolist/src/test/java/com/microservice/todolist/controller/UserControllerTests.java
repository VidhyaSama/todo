package com.microservice.todolist.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.todolist.config.JwtAuthenticationEntryPoint;
import com.microservice.todolist.dto.AuthenticationRequest;
import com.microservice.todolist.dto.MyUserDetails;
import com.microservice.todolist.dto.Response;
import com.microservice.todolist.dto.UserDto;
import com.microservice.todolist.dto.UserResponse;
import com.microservice.todolist.service.UserService;
import com.microservice.todolist.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
public class UserControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @InjectMocks
  UserController userController;

  @MockBean
  private UserService userService;

  @Mock
  private AuthenticationManager authenticationManager;

  @MockBean
  private JwtTokenUtil jwtTokenUtil;

  @MockBean
  private UserDetailsService userDetailsService;

  @MockBean
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


  ObjectMapper mapper = new ObjectMapper();

  private UserDto userDto;

  String token = "eyJhbGciOiJIUzUxMiJ9."
      + "eyJzdWIiOiJ2aWRoeWFAZ21haWwuY29tIiwiZXhwIjoxNjQyNDM3MTQ2LCJpYXQiOjE2NDI0MTkxNDZ9."
      + "Bv5OMK2O4xHs5RhDh_x9EojT_vKxfrlsGHWzpEPznddF-N4PlVaCy8jeTDuxZxrJyGLd002cfqUlLSTw9Sotqg";

  /*
   * @BeforeEach public void init() { UserDetails user = new
   * MyUserDetails("Vidhya@gmail.com", "vidhya123$");
   * SecurityContextHolder.getContext().setAuthentication(new
   * UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
   * Mockito.when(userService.loadUserByUsername(Mockito.anyString())).thenReturn(
   * user); Mockito.when(passwordEncoder.matches(mock,
   * Mockito.anyString())).thenReturn(true); }
   */

  @BeforeEach
  public void setUp() {
    userDto = UserDto.builder().id("Ga6JPebMFZXjiqF75LHcTG7L")
        .age(23).email("Vidhya@gmail.com").name("Sama Vidhya").build();
  }

  @SuppressWarnings("deprecation")
  @Test
  public void registerUserTest() throws Exception {
    String request = mapper.writeValueAsString(userRequest);
    Mockito.when(userService.saveUser(any(UserDto.class))).thenReturn(userDto);
    UserDetails userDetails = new MyUserDetails(userRequest.getEmail(), userRequest.getPassword());
    Mockito.when(userService.loadUserByUsername(userRequest.getEmail())).thenReturn(userDetails);
    Mockito.when(jwtTokenUtil.generateToken(userDetails)).thenReturn("ehiju");
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.post("/user/register")
            .content(request).contentType(MediaType.APPLICATION_JSON)).andReturn();
    assertEquals(201, result.getResponse().getStatus());
  }

  @Test
  public void createAuthenticationToken() throws Exception {
    AuthenticationRequest authRequest = AuthenticationRequest.builder()
        .email("Vidhya@gmail.com").password("Vidhya123$").build();
    String request = mapper.writeValueAsString(authRequest);
    UserDetails userDetails = new MyUserDetails(authRequest.getEmail(), authRequest.getPassword());
    Mockito.when(userService.loadUserByUsername(authRequest.getEmail())).thenReturn(userDetails);
    Mockito.when(jwtTokenUtil.generateToken(userDetails)).thenReturn("ehiju");
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.post("/user/login").content(request)
            .contentType(MediaType.APPLICATION_JSON)).andReturn();
    assertEquals(200, result.getResponse().getStatus());
  }

  @SuppressWarnings("deprecation")

  @Test
  public void updateUserTest() throws Exception {   
    String request = "{\"age\": 45}";
    userDto.setAge(45);
    Mockito.when(userService.updateUser(any(String.class), any(UserResponse.class)))
        .thenReturn(userDto);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/user/me").content(request)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)).andReturn();
    String responseStr = result.getResponse().getContentAsString();
    mapper.disable(MapperFeature.USE_ANNOTATIONS);
    Response response = mapper.readValue(responseStr, Response.class);
    assertEquals(userDto.getAge(), response.getUser().getAge());

  }

  @Test
  public void uploadImageTest() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "avatar", "person.jfif", "MediaType.IMAGE_JPEG_VALUE",
        new byte[1024]);
    Mockito.when(userService.uploadImage(any(String.class), any(byte[].class))).thenReturn(true);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .multipart("/user/me/avatar").file(file)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)).andReturn();
    String responseStr = result.getResponse().getContentAsString();
    Response response = mapper.readValue(responseStr, Response.class);
    assertEquals(true, response.getSuccess());
  }

  @Test
  public void uploadImageNegativeTest() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "avatar", null, "MediaType.IMAGE_JPEG_VALUE", new byte[0]);
    Mockito.when(userService.uploadImage(any(String.class), any(byte[].class))).thenReturn(true);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .multipart("/user/me/avatar").file(file)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)).andReturn();
    assertEquals("No image Found", result.getResolvedException().getLocalizedMessage());
  }

  @Test
  public void getUserImageTest() throws Exception {
    Mockito.when(userService.getImage(any(String.class), any(String.class))).thenReturn(new byte[1024]);
    MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.get("/user/{id}/avatar", "Ga6JPebMFZXjiqF75LHcTG7L")
        .contentType(MediaType.IMAGE_JPEG_VALUE)
        .header("Authorization", "Bearer " + token)).andReturn();
    assertEquals(200, result.getResponse().getStatus());
  }

  @Test
  public void getUserFromTokenTest() throws Exception {
    Mockito.when(userService.getUser(any(String.class))).thenReturn(userDto);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/me")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)).andReturn();
    String responseStr = result.getResponse().getContentAsString();
    UserDto user = mapper.readValue(responseStr, UserDto.class);
    assertEquals("Vidhya@gmail.com", user.getEmail());
  }
  
  @Test
  public void deleteImageTest() throws Exception {
    Mockito.when(userService.deleteImage(any(String.class))).thenReturn(true);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .delete("/user/me/avatar").contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)).andReturn();
    String responseStr = result.getResponse().getContentAsString();
    Response response = mapper.readValue(responseStr, Response.class);
    assertEquals(true, response.getSuccess());
  }
  
  @Test
  public void deleteUserTest() throws Exception {
    Mockito.when(userService.deleteUser(any(String.class))).thenReturn(userDto);
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .delete("/user/me").contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)).andReturn();
    assertEquals(200, result.getResponse().getStatus());
  }
}
