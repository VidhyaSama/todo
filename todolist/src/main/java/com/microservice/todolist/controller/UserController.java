package com.microservice.todolist.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.microservice.todolist.dto.AuthenticationRequest;
import com.microservice.todolist.dto.Response;
import com.microservice.todolist.dto.UserDto;
import com.microservice.todolist.dto.UserResponse;
import com.microservice.todolist.exception.ImageNotFoundException;
import com.microservice.todolist.exception.InvalidCredentialsException;
import com.microservice.todolist.service.UserService;
import com.microservice.todolist.util.JwtTokenUtil;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
/**
 * User API.
 *
 * @author vidhya.sama
 *
 */
@RestController
@RequestMapping("/user")
@OpenAPIDefinition(info = @Info(title = "User API"))
public class UserController {
  @Autowired
  private UserService userService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  /**
   * Display the user image.
   *
   * @param token The token identifies the user.
   * @param userId that identifies the user whose image to be displayed.
   * @return user image.
   */
  @GetMapping(value = "{id}/avatar", produces = { MediaType.,
      MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE })
  @Operation(summary = "Get User Image with UserId and Token")
  public ResponseEntity<?> getUserImage(
      @RequestHeader("Authorization") final String token,
      @PathVariable("id") final String userId) throws IOException {
    byte[] image = userService.getImage(token, userId);
    return new ResponseEntity<>(image, HttpStatus.OK);
  }

  /**
   * Get logged in user from token.
   *
   * @param token The token identifies the user.
   * @return userDto.
   */
  @GetMapping("/me")
  @Operation(summary = "Get User from Token")
  public ResponseEntity<?> getUserFromToken(
      @RequestHeader("Authorization") final String token) {
    UserDto userDto = userService.getUser(token);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  /**
   * Display the user image.
   *
   * @param token that identifies the user whose image to be deleted.
   * @return true.
   */

  @DeleteMapping("/me/avatar")
  @Operation(summary = "Delete User Image")
  public ResponseEntity<?> deleteUserImage(
      @RequestHeader("Authorization") final String token) {
    Boolean success = userService.deleteImage(token);
    return new ResponseEntity<>(Response.builder().success(success).build(),
        HttpStatus.OK);
  }

  /**
   * Delete logged in user.
   *
   * @param token The token identifies the user the data to be update.
   * @return userDto.
   */

  @DeleteMapping("/me")
  @Operation(summary = "Delete User")
  public ResponseEntity<?> deleteUserFromToken(
      @RequestHeader("Authorization") final String token) {
    UserDto userDto = userService.deleteUser(token);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  /**
   * This method is used to register the user.<br>
   *
   * @param userDto This contains user Details that has to be saved.
   * @return a saved user record and token.
   */
  @PostMapping("/register")
  @Operation(summary = "Add User")
  public ResponseEntity<?> registerUser(
      @Valid @RequestBody final UserDto userDto) {
    UserDto user = userService.saveUser(userDto);
    final UserDetails userDetails = userService
        .loadUserByUsername(user.getEmail());
    final String token = jwtTokenUtil.generateToken(userDetails);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Response.builder().user(userDto).token(token).build());
  }

  /**
   * This method is used to authenticate the user.<br>
   *
   * @param request contains email and password to authenticate.
   * @return a fully populated user record and token.
   *
   */

  @PostMapping("/login")
  @Operation(summary = "User Login")
  public ResponseEntity<?> createAuthenticationToken(
      @RequestBody final AuthenticationRequest request) throws Exception {
    try {
      userService.authenticate(request.getEmail(), request.getPassword());
    } catch (Exception e) {
      throw new InvalidCredentialsException("Unable to login");
    }
    final UserDetails userDetails = userService
        .loadUserByUsername(request.getEmail());
    final String token = jwtTokenUtil.generateToken(userDetails);
    UserDto userDto = userService.getUserByUsername(request.getEmail());
    return ResponseEntity
        .ok(Response.builder().user(userDto).token(token).build());
  }

  /**
   * Upload user image.
   *
   * @param token  The token identifies the user the data to be update.
   * @param avatar image of the user.
   * @return true if the user image save return false.
   */
  @PostMapping("/me/avatar")
  @Operation(summary = "Upload user Image")
  public ResponseEntity<?> uploadImage(
      @RequestHeader("Authorization") final String token,
      @RequestParam("avatar") final  MultipartFile avatar) throws IOException {
    if (avatar.getSize() <= 0) {
      throw new ImageNotFoundException("No image Found");
    }
    Boolean uploadStatus = userService.uploadImage(token, avatar.getBytes());
    return uploadStatus
        ? new ResponseEntity<>(Response.builder().success(true).build(),
            HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Update User .
   *
   * @param token The token identifies the user the data to be update.
   * @param userResponse contains fields that to be updated.
   * @return a fully populated user record.
   */
  @PutMapping("/me")
  @Operation(summary = "Update User Details")
  public ResponseEntity<?> updateUser(
      @RequestHeader("Authorization") final String token,
      @Valid @RequestBody final UserResponse userResponse) {
    UserDto userDto = userService.updateUser(token, userResponse);
    return ResponseEntity
        .ok(Response.builder().user(userDto).success(true).build());
  }
}
