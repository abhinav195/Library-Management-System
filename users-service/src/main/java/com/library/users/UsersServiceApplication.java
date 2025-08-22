package com.library.users;

import com.library.users.entity.Role;
import com.library.users.entity.RoleName;
import com.library.users.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UsersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner initRoles(RoleRepository repo) {
		return args -> {
			for (RoleName rn : RoleName.values()) {
				repo.findById(rn).orElseGet(() -> repo.save(new Role(rn)));
			}
		};
	}
}
