package com.microservice.todolist.service;

import com.microservice.todolist.dto.MyUserDetails;
import com.microservice.todolist.dto.UserDto;
import com.microservice.todolist.dto.UserResponse;
import com.microservice.todolist.entity.Task;
import com.microservice.todolist.entity.User;
import com.microservice.todolist.exception.ImageNotFoundException;
import com.microservice.todolist.repository.TaskRepository;
import com.microservice.todolist.repository.UserRepository;
import com.microservice.todolist.util.JwtTokenUtil;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

/**
 * UserServiceImp implements UserService to fetch and save the user and
 * implements UserDetailsService to load the user specific data.<br>
 *
 * @author vidhya.sama
 *
 */

@Service
@Slf4j
public class UserServiceImp implements UserService, UserDetailsService  {

  /**
   * Inject bean of type TaskRepository that perform crud operations on User
   * Table.
   *
   */
  @Autowired
  private UserRepository userRepository;

  /**
   * Inject bean of type TaskRepository that perform crud operations on task
   * Table.
   *
   */
  @Autowired
  private TaskRepository taskRepository;

  /**
   * Inject bean of type PasswordEncoder that process password encoding.
   *
   */
  @Autowired
  private PasswordEncoder bcryptEncoder;

  /**
   * Inject bean of type JwtTokenUtil that process JWT operations.
   *
   */
  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  /**
   * Inject bean of type AuthenticationManager that process authentication
   * request.
   */
  @Autowired
  private AuthenticationManager authenticationManager;

  /**
   * UseDto identifier to copy values from User Entity.
   */
  private UserDto userResonse = new UserDto();

  /**
   * int identifier to specify the length of id.
   */
  public static final int ID_LENGTH = 24;

  /**
   * This method is used to save the user.<br>
   *
   * @param userDto This contains user Details that has to be saved
   * @return a {@Code UserDto} instance.
   */
  @Override
  public UserDto saveUser(final UserDto userDto) {
    User user = new User();
    BeanUtils.copyProperties(userDto, user);
    user.setId(RandomStringUtils.randomAlphanumeric(ID_LENGTH));
    user.setPassword(bcryptEncoder.encode(user.getPassword()));
    user = userRepository.save(user);
    BeanUtils.copyProperties(user, userDto);
    return userDto;
  }

  /**
   * This method is used to fetch the user based on emailId.<br>
   *
   * @param email To identify the user whose data is required
   * @return a {@Code UserDto} instance.
   */

  @Override
  public UserDto getUserByUsername(final String email) {
    User user = userRepository.findByEmail(email);
    BeanUtils.copyProperties(user, userResonse);
    return userResonse;

  }

  /**
   * Locates the user based on the username from DB for Authentication.
   *
   * @param username the username identifying the user whose data is required
   * @return a {@Code UserDetails} instance.
   * @throws UserNotFoundException if the user could not be found
   */
  @Override
  public UserDetails loadUserByUsername(final String username)
      throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username);
    if (user == null) {
      log.error("User with email:{}" + username + "{}not found");
      throw new UsernameNotFoundException(
          "User with email:{}" + username + "{}not found");
    }
    return new MyUserDetails(user.getPassword(), user.getEmail());
  }

  /**
   * Update User .
   *
   * @param token        The token identifies the user the data to be update
   * @param userResponse contains fields that to be updated
   * @return a {@Code UserDto} instance.
   */
  @Override
  public UserDto updateUser(final String token,
      final UserResponse userResponse) {
    User user = userRepository.findByEmail(getUsername(token));
    user = mapDtoToEntity(userResponse, user);
    user = userRepository.save(user);
    BeanUtils.copyProperties(user, userResonse);
    return userResonse;
  }

  /**
   * Get user from token.
   *
   * @param token The token identifies the user the data to be update
   * @return a {@Code UserDto} instance.
   */
  @Override
  public UserDto getUser(final String token) {
    User user = userRepository.findByEmail(getUsername(token));
    BeanUtils.copyProperties(user, userResonse);
    return userResonse;
  }

  /**
   * Delete user from token.
   *
   * @param token The token identifies the user the record to be deleted
   * @return a {@Code UserDto} instance.
   */
  @Override
  public UserDto deleteUser(final String token) {
    User user = userRepository.findByEmail(getUsername(token));
    BeanUtils.copyProperties(user, userResonse);
    if (user.getTasks().size() != 0) {
      for (Task task : user.getTasks()) {
        taskRepository.delete(task);
      }
    }
    userRepository.delete(user);
    return userResonse;
  }

  /**
   * Upload user image.
   *
   * @param token The token identifies the user the data to be update
   * @param image user image
   * @return true if the user image save return false
   */
  @Override
  public Boolean uploadImage(final String token, final byte[] image) {
    User user = userRepository.findByEmail(getUsername(token));
    user.setImage(image);
    try {
      userRepository.save(user);
      return true;
    } catch (Exception e) {
      log.error("Error" + e);
      return false;
    }

  }

  /**
   * Display the user image.
   *
   * @param token  The token identifies the user the data to be update
   * @param userId that identifies the user whose image to be displayed
   * @return user image
   */
  @Override
  public byte[] getImage(final String token, final String userId) {
    Optional<User> user = userRepository.findByIdAndEmail(userId,
        getUsername(token));
    if (user.get().getImage() == null) {
      throw new ImageNotFoundException("No image Found");
    }
    return user.get().getImage();
  }

  /**
   * Display the user image.
   *
   * @param token that identifies the user whose image to be deleted
   * @return true
   */
  @Override
  public Boolean deleteImage(final String token) {

    User user = userRepository.findByEmail(getUsername(token));
    if (user.getImage() == null) {
      throw new ImageNotFoundException("No image Found");
    }
    user.setImage(null);
    userRepository.save(user);
    return true;
  }

  // map Dto to entity
  private User mapDtoToEntity(final UserResponse response, final User user) {
    if (Optional.ofNullable(response.getName()).isPresent()) {
      user.setName(response.getName().get());
    }
    if (Optional.ofNullable(response.getPassword()).isPresent()) {
      user.setPassword(bcryptEncoder.encode(response.getPassword().get()));
    }
    if (Optional.ofNullable(response.getEmail()).isPresent()) {
      user.setEmail(response.getEmail().get());
    }
    if (Optional.ofNullable(response.getAge()).isPresent()) {
      user.setAge(response.getAge().get());
    }
    return user;
  }

  /**
   * Fetch emailId from token.
   *
   * @param token that identifies the user emailId
   * @return emailId
   */
  public String getUsername(final String token) {
    String jwtToken = jwtTokenUtil.parseJwt(token);
    String emailId = jwtTokenUtil.getUsernameFromToken(jwtToken);
    return emailId;
  }

  /**
   * Attempts to authenticate the logged in user.
   *
   * @param username
   * @param password
   *
   */
  public void authenticate(final String username, final String password)
      throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
      log.info("Attempts to authentication passed");
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      log.error("invalid user credentials");
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }

}
