package com.apptime.auth.controller;

import com.apptime.auth.model.Task;
import com.apptime.auth.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.apptime.auth.controller.JsonUtil.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Qi Zhang
 * The unit class for TaskCategoryController
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskManagerTest {
    private static final String USERNAME = "username";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        taskRepository.deleteAll();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(USERNAME);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testGetTasks() throws Exception {
        Task task1 = createTask();
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verifyResultsWithName(actions, 1, null, task1.getName());

        Task task2 = createTask();
        actions = mockMvc.perform(MockMvcRequestBuilders.get("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verifyResultsWithName(actions, 2, null, task1.getName(), task2.getName());

        // add one task with different user
        Task taskWithDifferentUser = new Task();
        taskWithDifferentUser.setName(UUID.randomUUID().toString());
        taskWithDifferentUser.setUserName(UUID.randomUUID().toString());
        taskRepository.save(taskWithDifferentUser);

        actions = mockMvc.perform(MockMvcRequestBuilders.get("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verifyResultsWithName(actions, 2, taskWithDifferentUser.getName(), task1.getName(), task2.getName());

        // delete all
        taskRepository.deleteAll();
        actions = mockMvc.perform(MockMvcRequestBuilders.get("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verifyResultsWithName(actions, 0, taskWithDifferentUser.getName());
    }

    private Task createTask() {
        Task task = new Task();
        task.setName(UUID.randomUUID().toString());
        task.setUserName(USERNAME);
        taskRepository.save(task);
        return task;
    }

    @SuppressWarnings("unchecked")
    private void verifyResultsWithName(ResultActions actions, int expectedSize, String unexpectedName, String... expectedNames) throws UnsupportedEncodingException, JsonProcessingException {
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = mapper.readValue(content, List.class);
        assertEquals(expectedSize, list.size());
        for (Map<String, Object> map : list) {
            String name = (String) map.get("name");
            if (unexpectedName != null) {
                assertNotEquals(unexpectedName, name);
            }
            boolean found = false;
            for (String n : expectedNames) {
                if (n.equals(name)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
            assertEquals(USERNAME, map.get("userName"));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetTask() throws Exception {
        Task task = createTask();
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/tasks/task/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(content, Map.class);
        assertFalse(map.isEmpty());
        assertEquals(task.getName(), map.get("name"));
        assertEquals(USERNAME, map.get("userName"));
        Long id = ((Number) map.get("id")).longValue();
        assertNotNull(id);
        assertEquals(task.getId(), id.longValue());

        // add one task with different user
        Task taskWithDifferentUser = new Task();
        taskWithDifferentUser.setName(UUID.randomUUID().toString());
        taskWithDifferentUser.setUserName(UUID.randomUUID().toString());
        taskRepository.save(taskWithDifferentUser);
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/task/" + taskWithDifferentUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUpdateTask() throws Exception {
        Task task = createTask();

        Task request = new Task();
        String name = UUID.randomUUID().toString();
        request.setName(name);
        request.setId(task.getId());
        request.setDescription(UUID.randomUUID().toString());
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.put("/tasks/task")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(content, Map.class);
        assertFalse(map.isEmpty());
        assertEquals(request.getName(), map.get("name"));
        assertEquals(request.getDescription(), map.get("description"));
        assertEquals(USERNAME, map.get("userName"));
        Long id = ((Number) map.get("id")).longValue();
        assertNotNull(id);
        assertEquals(task.getId(), id.longValue());

        Task taskInDb = taskRepository.findById(id);
        assertNotNull(taskInDb);
        assertEquals(request.getName(), taskInDb.getName());
        assertEquals(USERNAME, taskInDb.getUserName());
        assertEquals(request.getDescription(), taskInDb.getDescription());

        // add one task with different user
        Task taskWithDifferentUser = new Task();
        taskWithDifferentUser.setName(UUID.randomUUID().toString());
        taskWithDifferentUser.setUserName(UUID.randomUUID().toString());
        taskRepository.save(taskWithDifferentUser);
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/task")
                .content(asJsonString(taskWithDifferentUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateTask() throws Exception {
        Task request = new Task();
        String name = UUID.randomUUID().toString();
        request.setName(name);
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/tasks/newtask")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(content, Map.class);
        assertFalse(map.isEmpty());
        assertEquals(name, map.get("name"));
        assertEquals(USERNAME, map.get("userName"));
        Long id = ((Number) map.get("id")).longValue();
        assertNotNull(id);
        Task taskInDb = taskRepository.findById(id);
        assertNotNull(taskInDb);
        assertEquals(name, taskInDb.getName());
        assertEquals(USERNAME, taskInDb.getUserName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDeleteTask() throws Exception {
        Task task = createTask();
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/task/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(content, Map.class);
        assertFalse(map.isEmpty());
        assertEquals(task.getName(), map.get("name"));
        assertEquals(USERNAME, map.get("userName"));
        Long id = ((Number) map.get("id")).longValue();
        assertNotNull(id);
        assertEquals(task.getId(), id.longValue());

        assertNull(taskRepository.findById(id));

        // add one task with different user
        Task taskWithDifferentUser = new Task();
        taskWithDifferentUser.setName(UUID.randomUUID().toString());
        taskWithDifferentUser.setUserName(UUID.randomUUID().toString());
        taskRepository.save(taskWithDifferentUser);
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/task/" + taskWithDifferentUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }
}
