package com.example.loginregistration;

import com.example.loginregistration.entity.Role;
import com.example.loginregistration.entity.User;
import com.example.loginregistration.repository.UserRepository;
import com.example.loginregistration.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    public void testLoadUserByUsername_UserFound() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password");
        Role role = new Role("ROLE_USER");
        user.setRoles(Collections.singleton(role));

        when(userRepository.findByUsernameOrEmail("testuser", "testuser"))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals(1, userDetails.getAuthorities().size());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsernameOrEmail("unknown", "unknown"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown");
        });
    }
}