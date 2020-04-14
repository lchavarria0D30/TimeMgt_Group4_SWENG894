package com.apptime.auth.controller;

import com.apptime.auth.config.TaskStateMachine;
import com.apptime.auth.model.FormatedDate;
import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskState;
import com.apptime.auth.repository.TaskReportRepository;
import com.apptime.auth.repository.TaskRepository;
import com.apptime.auth.service.TaskManagerService;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.apptime.auth.controller.JsonUtil.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Qi Zhang
 * The unit class for TaskCategoryController
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskManagerTest extends AbstractControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskManagerService taskManagerService;

    @Autowired
    private TaskReportRepository reportRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        cleanup();

        initMvc();
        mockAuthentication();
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

    @Test
    @SuppressWarnings("unchecked")
    public void testShowAddedSince() throws Exception {
        FormatedDate start = new FormatedDate();
        String sDate="2020-03-22";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date=dateFormat.parse(sDate);
        start.setDate(sDate);
        Task task1 = createTaskWithDueDate(date,"task1");
        String body = (new ObjectMapper()).valueToTree(start).toString();
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/tasks/due/start")
            .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = mapper.readValue(content, List.class);
        assertEquals(1, list.size());
        Map<String, Object> map = list.iterator().next();
        assertEquals(task1.getId(), ((Number) map.get("id")).longValue());
        assertEquals(task1.getName(), map.get("name"));

        sDate="2020-04-22";
        start.setDate(sDate);

        body = (new ObjectMapper()).valueToTree(start).toString();
        actions = mockMvc.perform(MockMvcRequestBuilders.post("/tasks/due/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertTrue(list.isEmpty());

        mockAuthentication("wronguser");
        sDate="2020-03-22";
        start.setDate(sDate);
        body = (new ObjectMapper()).valueToTree(start).toString();
        actions = mockMvc.perform(MockMvcRequestBuilders.post("/tasks/due/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertTrue(list.isEmpty());
    }

    private Task createTaskWithDueDate(Date start, String name) {
        Task task = createTask();
        task.setScheduledstart(start);
        taskRepository.save(task);
        return task;
    }

    private Task createTask() {
        return createTaskWithUsername(USERNAME);
    }

    private Task createTaskWithUsername(String username) {
        Task task = new Task();
        task.setName(UUID.randomUUID().toString());
        task.setUserName(username);
        TaskStateMachine.CREATE(task);
        taskRepository.save(task);
        return task;
    }

    private Task createAndStartTask() {
        Task task = createTask();
        TaskStateMachine.START(task);
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

        Task taskInDb = taskManagerService.getTask(id);
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
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateTask() throws Exception {
        Task request = new Task();
        String name = UUID.randomUUID().toString();
        request.setName(name);
        request.setScheduledstart(new Date());
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
        Task taskInDb = taskManagerService.getTask(id);
        assertNotNull(taskInDb);
        assertEquals(name, taskInDb.getName());
        assertEquals(USERNAME, taskInDb.getUserName());
        assertEquals(TaskState.CREATED, taskInDb.getState());
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

        assertFalse(taskRepository.findById(id).isPresent());

        // add one task with different user
        Task taskWithDifferentUser = new Task();
        taskWithDifferentUser.setName(UUID.randomUUID().toString());
        taskWithDifferentUser.setUserName(UUID.randomUUID().toString());
        taskRepository.save(taskWithDifferentUser);
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/task/" + taskWithDifferentUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testStartTask() throws Exception {
        Task task = createTask();
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/tasks/task/" + task.getId() + "/start")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.readValue(content, String.class);
        assertNotNull(value);
        assertEquals(TaskState.ACTIVE.toString(), value);

        Task taskInDb = taskManagerService.getTask(task.getId());
        assertNotNull(taskInDb);
        assertEquals(TaskState.ACTIVE, taskInDb.getState());
        assertNotNull(taskInDb.getStart());

        Random r = new Random();
        long unexistingId = r.nextLong();
        while (unexistingId == task.getId()) {
            unexistingId = r.nextLong();
        }
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/task/" + unexistingId + "/start")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        // create another task
        Task task2 = createTask();
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/task/" + task2.getId() + "/start")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

        // clean db
        taskRepository.deleteAll();

        // create one task with different username
        task = createTaskWithUsername(UUID.randomUUID().toString());
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/task/" + task.getId() + "/start")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testPauseTask() throws Exception {
        Task task = createAndStartTask();

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/tasks/task/" + task.getId() + "/pause")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.readValue(content, String.class);
        assertNotNull(value);
        assertEquals(TaskState.PAUSED.toString(), value);

        Task taskInDb = taskManagerService.getTask(task.getId());
        assertNotNull(taskInDb);
        assertEquals(TaskState.PAUSED, taskInDb.getState());
        assertNotNull(taskInDb.getStart());
        assertNotNull(taskInDb.getDuration());

        Random r = new Random();
        long unexistingId = r.nextLong();
        while (unexistingId == task.getId()) {
            unexistingId = r.nextLong();
        }
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/task/" + unexistingId + "/pause")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        // clean db
        taskRepository.deleteAll();

        // create one task with different username
        task = createTaskWithUsername(UUID.randomUUID().toString());
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/task/" + task.getId() + "/pause")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testCompleteTask() throws Exception {
        Task task = createAndStartTask();

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/tasks/task/" + task.getId() + "/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.readValue(content, String.class);
        assertNotNull(value);
        assertEquals(TaskState.COMPLETED.toString(), value);

        Task taskInDb = taskManagerService.getTask(task.getId());
        assertNotNull(taskInDb);
        assertEquals(TaskState.COMPLETED, taskInDb.getState());
        assertNotNull(taskInDb.getStart());
        assertNotNull(taskInDb.getDuration());

        Random r = new Random();
        long unexistingId = r.nextLong();
        while (unexistingId == task.getId()) {
            unexistingId = r.nextLong();
        }
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/task/" + unexistingId + "/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        // clean db
        taskRepository.deleteAll();

        // create one task with different username
        task = createTaskWithUsername(UUID.randomUUID().toString());
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/task/" + task.getId() + "/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }
}
