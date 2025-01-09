package com.ums.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ums.dto.LoginRequest;
import com.ums.dto.LoginResponse;
import com.ums.dto.UserRequest;
import com.ums.dto.UserResponse;
import com.ums.entity.User;
import com.ums.exception.InvalidUsernameException;
import com.ums.exception.SessionExpiredException;
import com.ums.exception.UserNotFoundException;
import com.ums.exception.UsernameAlreadyExistException;
import com.ums.jwt.JwtService;
import com.ums.mapper.UserMapper;
import com.ums.repository.UserRepo;
import com.ums.security.CustomUserDetailsService;
import com.ums.util.CacheHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

	private UserMapper mapper;
	private UserRepo userRepo;
	private EmailService emailService;
	private CacheHelper cacheHelper;
	private JwtService jwtService;
	private AuthenticationManager authenticationManager;
	private CustomUserDetailsService customUserDetailsService;

	public UserResponse register(UserRequest userRequest) {

		Optional<User> optional = userRepo.findByUsername(userRequest.getUsername());

		if (optional.isEmpty()) {

			User user = mapper.mapToUser(userRequest);
			userRepo.save(user);
			return mapper.mapToUserResponse(user);
		}

		throw new UsernameAlreadyExistException("Username Exists, Try other");
	}

	public List<UserResponse> getAll() {

		List<User> all = userRepo.findAll();

		if (all.isEmpty())

			throw new UserNotFoundException("No Users IN DB !!!");

		return all.stream().map(user -> {
			return mapper.mapToUserResponse(user);
		}).collect(Collectors.toList());
	}

	public Object getUser(String username) {

		Optional<User> optional = userRepo.findByUsername(username);

		if (optional.isEmpty())
			throw new UserNotFoundException("Invalid Username, Check your username !!!");

		var user = optional.get();
		int otp = emailService.generateOtp();

		cacheHelper.userCache(user);
		cacheHelper.otpCache(otp, user.getUsername());

		return emailService.mailSender(user.getEmail(), otp);

	}

	public UserResponse retrieveEmloyee(String username, Integer otp, UserRequest request)
			throws InvalidUsernameException, SessionExpiredException {

		User user = cacheHelper.getUserCache(username);

		log.info("" + user);

		Integer otpCache = cacheHelper.getOtpCache(username);
		if (!otpCache.equals(otp))
			throw new SessionExpiredException("Maye be wrorng OTP / Session Expired !!!");

		if (!username.equals(user.getUsername()))
			throw new InvalidUsernameException("Incorrect Username !!!");



		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
		user.setRoles(request.getRoles());
		user.setModifiedAt(LocalDateTime.now());

		userRepo.save(user);

		cacheHelper.removeOtpCache(username);
		cacheHelper.removeUserCache(username);

		return mapper.mapToUserResponse(user);
	}

	public LoginResponse login(LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(

				new UsernamePasswordAuthenticationToken(

						loginRequest.getUsername(),
						loginRequest.getPassword()
						)
				);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		LoginResponse response = new LoginResponse();

		response.setAccessToken(jwtService.getAccessToken(authentication));
		response.setRefreshToken(jwtService.getRefreshToken(authentication));

		return response;
	}

	public Object refreshToken(HttpServletRequest request, HttpServletResponse response) {

		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		String refreshToken = header.substring(7);
		
		LoginResponse loginResponse = new LoginResponse();

		if(refreshToken == null || !header.startsWith("Bearer "))
			return "Invalid Token !!!";

		String username = jwtService.extractUsername(refreshToken);

		if(username != null) {

			UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

			if(userDetails != null && jwtService.isValidToken(refreshToken)) {

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						username,
						userDetails.getPassword(),
						userDetails.getAuthorities()
						);
				String accessToken = jwtService.getAccessToken(authenticationToken);
				
				loginResponse.setAccessToken(accessToken);
				loginResponse.setRefreshToken(jwtService.getRefreshToken(authenticationToken));
			}
		}

		return loginResponse;
	}

}
