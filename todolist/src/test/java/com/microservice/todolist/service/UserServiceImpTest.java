package com.microservice.todolist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.microservice.todolist.dto.UserDto;
import com.microservice.todolist.dto.UserResponse;
import com.microservice.todolist.entity.Task;
import com.microservice.todolist.entity.User;
import com.microservice.todolist.exception.ImageNotFoundException;
import com.microservice.todolist.repository.TaskRepository;
import com.microservice.todolist.repository.UserRepository;
import com.microservice.todolist.util.JwtTokenUtil;

@SpringBootTest
public class UserServiceImpTest {
  @InjectMocks
  private UserServiceImp userService;
  
  @Mock
  private UserRepository userRepository;

  @Mock
  private TaskRepository taskRepository;

  @Mock
  private PasswordEncoder bcryptEncoder;

  @Mock
  private JwtTokenUtil jwtTokenUtil;
  
  @Mock
  private AuthenticationManager authenticationManager;
  
  @Mock
  private UserDetailsService userDetailsService;
  
  private UserDto userDto;
  
  private User user;

  String token = "eyJhbGciOiJIUzUxMiJ9."
      + "eyJzdWIiOiJ2aWRoeWFAZ21haWwuY29tIiwiZXhwIjoxNjQyNDM3MTQ2LCJpYXQiOjE2NDI0MTkxNDZ9."
      + "Bv5OMK2O4xHs5RhDh_x9EojT_vKxfrlsGHWzpEPznddF-N4PlVaCy8jeTDuxZxrJyGLd002cfqUlLSTw9Sotqg";
  
  @BeforeEach
  public void setUp() {
    userDto = UserDto.builder().id("Ga6JPebMFZXjiqF75LHcTG7L")
        .age(23).email("Vidhya@gmail.com").name("Sama Vidhya")
        .password("Mockito123$").build();  
    List<Task> task = new ArrayList<Task>();
    user = User.builder().id("Ga6JPebMFZXjiqF75LHcTG7L")
        .age(23).email("Vidhya@gmail.com").name("Sama Vidhya")
        .tasks(task).image(new byte[100]).build();
  }
  
  @Test
  public void saveUserTest() {
    User user = new User();
    BeanUtils.copyProperties(userDto, user);
    user.setId(RandomStringUtils.randomAlphanumeric(24));
    user.setPassword(bcryptEncoder.encode(user.getPassword()));
    Mockito.when(userRepository.save(any())).thenReturn(user);
    BeanUtils.copyProperties(user, userDto);
    UserDto result = userService.saveUser(userDto);
    assertEquals(userDto.getEmail(), result.getEmail());  
  }

  @Test
  public void getUserByUsernameTest() {
    Mockito.when(userRepository.findByEmail(any())).thenReturn(user);
    BeanUtils.copyProperties(user, userDto);
    UserDto result =  userService.getUserByUsername(userDto.getEmail());
    assertEquals(userDto.getEmail(), result.getEmail()); 
  }

  @Test
  public void loadByUserNameTest() {
    Mockito.when(userRepository.findByEmail(any())).thenReturn(user);
    UserDetails result =  userService.loadUserByUsername(userDto.getEmail());
    assertEquals(userDto.getEmail(), result.getUsername()); 
  }
  
  @Test
  public void loadByUserNameNegativeTest() {
    Mockito.when(userRepository.findByEmail(any())).thenReturn(null);
    assertThrows(UsernameNotFoundException.class, () -> {
      userService.loadUserByUsername(userDto.getEmail());
    });

  }
  
  @Test
  public void updateUserTest() {
    UserResponse response = new UserResponse();
    response.setName(Optional.of("Nicolas Lee"));
    Mockito.when(userRepository.findByEmail(any())).thenReturn(user);
    user.setName(response.getName().get());
    Mockito.when(userRepository.save(any())).thenReturn(user);
    BeanUtils.copyProperties(user, userDto);
    UserDto result =  userService.updateUser("Bearer " + token, response);    
    assertEquals("Nicolas Lee", result.getName()); 
  }
  
  @Test
  public void getUserTest() {
    Mockito.when(userRepository.findByEmail(any())).thenReturn(user);
    BeanUtils.copyProperties(user, userDto);
    UserDto result =  userService.getUser("Bearer " + token);
    assertEquals(userDto.getEmail(), result.getEmail()); 
  }
  
  @Test
  public void deleteUserTest() {
    Mockito.when(userRepository.findByEmail(any())).thenReturn(user);
    BeanUtils.copyProperties(user, userDto);
    doNothing().when(userRepository).delete(any());
    UserDto result =  userService.deleteUser("Bearer " + token);
    assertEquals(userDto.getEmail(), result.getEmail()); 
  }
  
  @Test
  public void uploadImageTest() {
    byte[] image = new byte[100];
    Mockito.when(userRepository.findByEmail(any())).thenReturn(user);
    user.setImage(image);
    Mockito.when(userRepository.save(any())).thenReturn(user);
    assertTrue(userService.uploadImage("Bearer " + token, image)); 
  }
  
  @Test
  public void getImageTest() {
    Mockito.when(userRepository.findByIdAndEmail(any(), any())).thenReturn(Optional.of(user));
    Mockito.when(userRepository.save(any())).thenReturn(user);
    byte[] result =  userService.getImage("Ga6JPebMFZXjiqF75LHcTG7L", "Bearer " + token);
    assertEquals(100, result.length); 
  }
  
  @Test
  public void getImageNegativeTest() {
    Mockito.when(userRepository.findByIdAndEmail(any(), any())).thenReturn(Optional.of(user));
    user.setImage(null);
    assertThrows(ImageNotFoundException.class, () -> {
      userService.getImage("Ga6JPebMFZXjiqF75LHcTG7L", "Bearer " + token);
    });
  }
  
  @Test
  public void deleteImageTest() {
    Mockito.when(userRepository.findByEmail(any())).thenReturn(user);
    Mockito.when(userRepository.save(any())).thenReturn(user);
    assertTrue(userService.deleteImage("Bearer " + token)); 
  }
  
  @Test
  public void deleteImageNegativeTest() {
    user.setImage(null);
    Mockito.when(userRepository.findByEmail(any())).thenReturn(user);
    assertThrows(ImageNotFoundException.class, () -> {
      userService.deleteImage("Bearer " + token);
    });
  }
  
  @Test
  public void getUsernameTest() {
    Mockito.when(jwtTokenUtil.parseJwt(any(String.class))).thenReturn(token);
    Mockito.when(jwtTokenUtil.getUsernameFromToken(any(String.class)))
        .thenReturn(user.getEmail());
    String email = userService.getUsername("Bearer " + token);
    assertEquals(user.getEmail(), email);
  }
  
}

