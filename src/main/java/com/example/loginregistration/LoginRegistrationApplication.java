package com.example.loginregistration;

import com.example.loginregistration.entity.Role;
import com.example.loginregistration.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class LoginRegistrationApplication {

    public static void main(String[] args) {

        SpringApplication.run(LoginRegistrationApplication.class, args);
    }

    @Component
    public class DataLoader implements CommandLineRunner {

        @Autowired
        private RoleRepository roleRepository;

        @Override
        public void run(String... args) {
            if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                roleRepository.save(new Role("ROLE_USER"));
            }
            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                roleRepository.save(new Role("ROLE_ADMIN"));
            }
        }
    }
}
