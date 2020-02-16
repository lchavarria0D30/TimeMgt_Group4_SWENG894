package com.apptime.auth.controller;

import com.apptime.auth.model.ResetPasswordRequest;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.Users;
import com.apptime.auth.model.to.Category;
import com.apptime.auth.repository.TaskCategoryRepository;
import com.apptime.auth.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.apptime.auth.controller.JsonUtil.asJsonString;
import static com.apptime.auth.controller.JsonUtil.parseJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Qi Zhang
 * The unit class for TaskCategoryController
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskCategoryControllerTest {
    private static final String USERNAME = "username";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskCategoryRepository categoryRepository;

    @Autowired
    private BCryptPasswordEncoder pEncoder;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        categoryRepository.deleteAll();
        createUser(USERNAME);
    }

    private void createUser(String username) {
        String password = UUID.randomUUID().toString();
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(pEncoder.encode(password));
        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = {"USER"})
    public void testCreatePrivateCategory() throws Exception {
        Category request = new Category();
        String name = UUID.randomUUID().toString();
        request.setName(name);
        mockMvc.perform(MockMvcRequestBuilders.post("/category")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

        List<TaskCategory> categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
        assertEquals(name, categories.iterator().next().getName());
        assertFalse(categories.iterator().next().isPublic());

        // create a category with same name
        mockMvc.perform(MockMvcRequestBuilders.post("/category")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
        categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());

        // create another category with different name
        String name2 = UUID.randomUUID().toString();
        Category request2 = new Category();
        request2.setName(name2);
        mockMvc.perform(MockMvcRequestBuilders.post("/category")
                .content(asJsonString(request2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

        categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty());
        assertEquals(2, categories.size());

        // get categories for current user
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/category/mine")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List list = mapper.readValue(content, List.class);
        assertEquals(2, list.size());
        for (Object obj : list) {
            Map<String, Object> map = (Map<String, Object>) obj;
            String cname = (String) map.get("name");
            boolean isPublic = (Boolean) map.get("public");
            assertTrue(cname.equals(name) || cname.equals(name2));
            assertFalse(isPublic);
        }
    }

}
