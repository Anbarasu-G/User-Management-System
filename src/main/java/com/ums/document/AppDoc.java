package com.ums.document;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition
public class AppDoc {

	Info info() {
		return new Info().title("User-Management-System-REStFul API")
				.version("v1")
				.description("User Creation & Registration : Allows the creation of new user accounts, including details like username, email, role, etc.\n"
						+ "\nAuthentication & Authorization : Manages login credentials (like passwords) and determines the permissions or access levels each user has based on their role (admin, manager, user, etc.).\n"
						+ "\nUser Roles & Permissions: Defines and assigns roles to users (e.g., admin, user, moderator) and controls access rights, ensuring only authorized users can perform certain actions.\n"
						+ "\nAccount Recovery: Provides features for users to recover their accounts, typically using methods like email  verification for password resets.\n"
						+ "\nUser Deactivation/Deletion: Allows administrators to deactivate or delete users who are no longer part of the organization or do not need access to certain services anymore.\n"
						+ "\nSecurity Features: Includes JSON Web Token (JWT), encryption of sensitive user data, and other security protocols to protect user information.\n");
	}

	@Bean
	OpenAPI openApi() {
		return new OpenAPI().info(info());
	}

}

