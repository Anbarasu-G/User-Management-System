package com.ums.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ums.dto.LoginRequest;
import com.ums.dto.UserRequest;
import com.ums.exception.InvalidUsernameException;
import com.ums.exception.SessionExpiredException;
import com.ums.exception.UserNotFoundException;
import com.ums.service.UserService;
import com.ums.util.AppResponseBuilder;
import com.ums.util.SuccessStructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

	private UserService userService;

	@Operation(description = "This end point is used to save the user to the database",
			responses = {
					@ApiResponse(responseCode = "201", description = "User  created successfully !!!"),
					@ApiResponse(responseCode = "208", description = "User already created / failed to create !!!",
					content = { @Content(schema = @Schema(anyOf = RuntimeException.class)) }
							)
	})
	@PostMapping("/register")
	public ResponseEntity<SuccessStructure<?>> register(@RequestBody UserRequest userRequest) {

		return ResponseEntity.ok(

				AppResponseBuilder.success(HttpStatus.CREATED.value(), "User Created Successfully !!!",
						userService.register(userRequest)));
	}

	@Operation(description = "This end point is used to fetch all the users to the database",
			responses = {
					@ApiResponse(responseCode = "302", description = "User  Found !!!"),
					@ApiResponse(responseCode = "404", description = "No User Found !!!",
					content = { @Content(schema = @Schema(anyOf = RuntimeException.class)) }
							)
	})
	@GetMapping("/users")
	public ResponseEntity<SuccessStructure<?>> getAll() {

		return ResponseEntity.ok(

				AppResponseBuilder.success(HttpStatus.FOUND.value(), "Users Found!!!", userService.getAll()));
	}

	@Operation(description = "This end point is used to fetch the users to the database",
			responses = {
					@ApiResponse(responseCode = "200", description = "User  Found, You will get mail to your Gmail !!!"),
					@ApiResponse(responseCode = "500", description = "No User Found !!!",
					content = { @Content(schema = @Schema(anyOf = UserNotFoundException.class)) }
							)
	})
	@GetMapping("/users/{username}")
	public ResponseEntity<?> getEmployee(@PathVariable(value = "username") String username) {

		return ResponseEntity.ok(userService.getUser(username));

	}

	@Operation(description = "This end point is used to update the users to the database,  it expects the username, otp which recieved to uour mail, Updated User details in JSON format",
			responses = {
					@ApiResponse(responseCode = "200", description = "User Updated,!!!"),
					@ApiResponse(responseCode = "500", description = "No User Found !!!",
					content = { @Content(schema = @Schema(anyOf = UserNotFoundException.class)) }
							)
	})
	@PutMapping("/users/{username}/{otp}")
	public ResponseEntity<?> verifyOtpToGetEmployee(

			@PathVariable(value = "username") String username, @PathVariable(value = "otp") Integer otp,
			@RequestBody UserRequest request

			) throws InvalidUsernameException, SessionExpiredException {

		return ResponseEntity.ok(

				AppResponseBuilder.success(HttpStatus.OK.value(), "Users Updated!!!",
						userService.retrieveEmloyee(username, otp, request)));
	}

	
	@Operation(description = "This end point is used to login /  get access Token (JWT)  it expects the username, password",
			responses = {
					@ApiResponse(responseCode = "202", description = "User Logged In Successfully !!!"),
					@ApiResponse(responseCode = "500", description = "No User Found !!!",
					content = { @Content(schema = @Schema(anyOf = UserNotFoundException.class)) }
							)
	})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

		return ResponseEntity.ok(

				AppResponseBuilder.success(HttpStatus.ACCEPTED.value(), "Logged in Successfully !!!",
						userService.login(loginRequest)));
	}

	@Operation(description = "This end point is used  to get refresh Token (JWT)  it expects the refresh-token which i got earlier and non-expired ",
			responses = {
					@ApiResponse(responseCode = "202", description = "Token Refreshed Successfully !!!"),
					@ApiResponse( description = "Token Invalid / expired Token !!!")
	})
	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(

			HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.ok(

				AppResponseBuilder.success(HttpStatus.ACCEPTED.value(), "Token Refreshed in Successfully !!!",
						userService.refreshToken(request, response)));
	}
}
