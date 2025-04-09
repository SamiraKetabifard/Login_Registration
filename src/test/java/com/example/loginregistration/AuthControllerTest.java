package com.example.loginregistration;

import com.example.loginregistration.dto.LoginDto;
import com.example.loginregistration.dto.SignUpDto;
import com.example.loginregistration.entity.Role;
import com.example.loginregistration.repository.RoleRepository;
import com.example.loginregistration.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testUserRegistration_Success() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("Test User");
        signUpDto.setUsername("testuser");
        signUpDto.setEmail("test@example.com");
        signUpDto.setPassword("password");
        signUpDto.setRole("user");

        Role role = new Role("ROLE_USER");

        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(roleRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(role));
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User registered successfully"));
    }

    @Test
    public void testUserRegistration_UsernameExists() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("existinguser");
        signUpDto.setEmail("test@example.com");
        signUpDto.setPassword("password");

        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUserLogin_Success() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("testuser");
        loginDto.setPassword("password");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk());
    }
}
