package com.apptime.auth.controller;

import static com.apptime.auth.controller.JsonUtil.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.UUID;

import com.apptime.auth.model.ResetPasswordRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apptime.auth.model.Roles;
import com.apptime.auth.model.Users;
import com.apptime.auth.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Autowired
    UserRepository userRepo;

    @Autowired
    MockMvc mockMvc;

    @Mock
    Users user;
    @Mock
    Roles role;

    @Autowired
    BCryptPasswordEncoder pEncoder;

    @BeforeEach
    public void init() {
        cleanup();
    }

    @AfterEach
    public void cleanup() {
        userRepo.deleteAll();
    }

    @Test
    public void testSignup() throws Exception {
        final String username = "username";
        final String password = "password";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail("user@test.com");
        Roles role = new Roles();
        role.setRole("USER");
        user.setRoles(Collections.singleton(role));

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "username", authorities = {"USER"})
    public void testSignin() throws Exception {
        final String username = "username";
        final String password = "password";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail("user@test.com");
        Roles role = new Roles();
        role.setRole("USER");
        user.setRoles(Collections.singleton(role));
        userRepo.save(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "username", authorities = {"USER"})
    public void testResetPassword() throws Exception{
        final String username = "username";
        final String password = "password";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(pEncoder.encode(password));
        user.setEmail("user@test.com");
        Roles role = new Roles();
        role.setRole("USER");
        user.setRoles(Collections.singleton(role));
        userRepo.save(user);

        String newPassword = "newpassword";
        ResetPasswordRequest request = new ResetPasswordRequest(username, password, newPassword);
        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        Users updatedUser = userRepo.findByUsername(username);
        assertTrue(pEncoder.matches(newPassword, updatedUser.getPassword()));

        request.setUsername(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

        request.setUsername(username);
        request.setOldPassword(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

        request.setOldPassword(password);
        request.setNewPassword(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

        // wrong username
        request.setNewPassword(newPassword);
        request.setUsername(UUID.randomUUID().toString());
        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());

        // wrong password
        request.setUsername(username);
        request.setOldPassword(UUID.randomUUID().toString());
        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }
}