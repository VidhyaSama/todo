/***Service Layer **/
package com.microservice.todolist.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.microservice.todolist.dto.UserDto;
import com.microservice.todolist.dto.UserResponse;

public interface UserService {

  /**
   * This method is used to save the user.<br>
   *
   * @param userDto This contains user Details that has to be saved
   * @return a {@Code UserDto} instance.
   */
  UserDto saveUser(UserDto userDto);

  /**
   * This method is used to fetch the user based on emailId.<br>
   *
   * @param username To identify the user whose data is required
   * @return a {@Code UserDto} instance.
   */
  UserDto getUserByUsername(String username);

  /**
   * Upload user image.
   *
   * @param token The token identifies the user the data to be update
   * @param image user image
   * @return true if the user image save return false
   */
  Boolean uploadImage(String token, byte[] image);

  /**
   * Display the user image.
   *
   * @param token  The token identifies the user the data to be update
   * @param userId that identifies the user whose image to be displayed
   * @return user image
   */
  byte[] getImage(String token, String userId);

  /**
   * Display the user image.
   *
   * @param token that identifies the user whose image to be deleted
   * @return true
   */
  Boolean deleteImage(String token);

  /**
   * Update User .
   *
   * @param token        The token identifies the user the data to be update
   * @param userResponse contains fields that to be updated
   * @return user
   */
  UserDto updateUser(String token, UserResponse userResponse);

  /**
   * Get user from token.
   *
   * @param token The token identifies the user the data to be update
   * @return a {@Code UserDto} instance.
   */
  UserDto getUser(String token);

  /**
   * Delete user from token.
   *
   * @param token The token identifies the user the record to be deleted
   * @return a {@Code UserDto} instance.
   */
  UserDto deleteUser(String token);

  /**
   * Locates the user based on the username from DB for Authentication.
   *
   * @param username the username identifying the user whose data is required
   * @return a {@Code UserDetails} instance.
   * @throws UserNotFoundException if the user could not be found
   */
  UserDetails loadUserByUsername(String username);

  /**
   * Attempts to authenticate the logged in user.
   *
   * @param username
   * @param password
   *
   */
  void authenticate(String username, String password) throws Exception;

  /**
   * Fetch emailId from token.
   *
   * @param token that identifies the user emailId
   * @return emailId
   */
  String getUsername(String token);

}
