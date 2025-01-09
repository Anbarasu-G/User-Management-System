package com.ums.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ums.jwt.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

	private CustomUserDetailsService customUserDetailsService;
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity  httpSecurity) throws Exception {

		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(request -> {

					request
					.requestMatchers(
							"/swagger-ui/**", 
							"/v3/api-docs/**",
							"api/register",
							"/api/login").permitAll()
					.anyRequest().authenticated();
//					.requestMatchers("/**").permitAll();
				})
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.formLogin(Customizer.withDefaults())
				.httpBasic(Customizer.withDefaults())
				.build();
	}

//	@Bean
//	public UserDetailsService userDetailsService() {
//
//		return customUserDetailsService;
//	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

//	@Bean
//	public AuthenticationProvider authenticationProvider() {
//
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); 
//
//		provider.setUserDetailsService(customUserDetailsService);
//		provider.setPasswordEncoder(passwordEncoder());
//
//		return provider;
//	}

	@Bean
	public AuthenticationManager authenticationManager(

			AuthenticationConfiguration authenticationConfiguration
			)
					throws Exception {

		return  authenticationConfiguration.getAuthenticationManager();
	}
}
