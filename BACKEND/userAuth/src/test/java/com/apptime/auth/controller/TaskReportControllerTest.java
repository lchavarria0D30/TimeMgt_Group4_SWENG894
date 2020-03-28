package com.apptime.auth.controller;

import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskReportType;
import com.apptime.auth.repository.TaskReportRepository;
import com.apptime.auth.repository.TaskRepository;
import com.apptime.auth.service.TaskManagerService;
import com.apptime.auth.service.TaskReportService;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.apptime.auth.service.impl.TaskReportServiceImpl.DATE_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Qi Zhang
 * This is the unit test class for TaskReportController
 * Use Case: TMGP4-26, TMGP4-31
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskReportControllerTest extends AbstractControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskReportRepository reportRepository;

    @Autowired
    private TaskReportService reportService;

    @Autowired
    private TaskManagerService taskManagerService;

    @BeforeEach
    public void init() {
        reportRepository.deleteAll();
        taskRepository.deleteAll();

        MockitoAnnotations.initMocks(this);
        initMvc();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetAllReports() throws Exception {
        mockAuthentication();

        Task task1 = new Task();
        Date start = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 12);
        task1.setScheduledstart(start);
        task1.setStart(start);
        Date scheduledEnd = new Date();
        Date end = new Date(System.currentTimeMillis() - 1000L * 60 * 10); // 10 min
        task1.setScheduledEnd(scheduledEnd);
        task1.setEnd(end);
        task1.setName(UUID.randomUUID().toString());
        task1.setUserName(USERNAME);
        taskRepository.save(task1);
        reportService.generateReport(task1);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/report")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = mapper.readValue(content, List.class);
        assertEquals(1, list.size());
        Map<String, Object> map = list.iterator().next();
        assertEquals(TaskReportType.EARLIER.name(), map.get("type"));
        assertEquals(task1.getId(), ((Number) map.get("taskId")).longValue());

        // create another task/report with different username
        Task task2 = new Task();
        task2.setScheduledstart(start);
        task2.setStart(start);
        task2.setScheduledEnd(scheduledEnd);
        task2.setEnd(new Date(System.currentTimeMillis() + 1000L * 60 * 60));
        task2.setName(UUID.randomUUID().toString());
        task2.setUserName(UUID.randomUUID().toString());
        taskRepository.save(task2);
        reportService.generateReport(task2);

        actions = mockMvc.perform(MockMvcRequestBuilders.get("/report")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertEquals(1, list.size());
        map = list.iterator().next();
        assertEquals(TaskReportType.EARLIER.name(), map.get("type"));

        // add another task/report
        Task task3 = new Task();
        task3.setScheduledstart(start);
        task3.setStart(start);
        end = new Date(System.currentTimeMillis() + 1000L * 60 * 10); // 10 min
        task3.setScheduledEnd(scheduledEnd);
        task3.setEnd(end);
        task3.setName(UUID.randomUUID().toString());
        task3.setUserName(USERNAME);
        taskRepository.save(task3);
        reportService.generateReport(task3);

        actions = mockMvc.perform(MockMvcRequestBuilders.get("/report")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertEquals(2, list.size());

        // get reports by time range
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        String startDate = dateFormat.format(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 5);
        actions = mockMvc.perform(MockMvcRequestBuilders.get("/report")
                .param("startDate", startDate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertEquals(2, list.size());

        startDate = dateFormat.format(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 5);
        actions = mockMvc.perform(MockMvcRequestBuilders.get("/report")
                .param("startDate", startDate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertEquals(0, list.size());

        String endDate = dateFormat.format(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 5);
        actions = mockMvc.perform(MockMvcRequestBuilders.get("/report")
                .param("endDate", endDate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertEquals(2, list.size());

        endDate = dateFormat.format(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 5);
        actions = mockMvc.perform(MockMvcRequestBuilders.get("/report")
                .param("endDate", endDate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertEquals(0, list.size());

        startDate = dateFormat.format(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 5);
        endDate = dateFormat.format(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 5);
        actions = mockMvc.perform(MockMvcRequestBuilders.get("/report")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertEquals(2, list.size());

        startDate = dateFormat.format(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 5);
        endDate = dateFormat.format(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 3);
        actions = mockMvc.perform(MockMvcRequestBuilders.get("/report")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertEquals(0, list.size());

        startDate = dateFormat.format(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 3);
        endDate = dateFormat.format(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 5);
        actions = mockMvc.perform(MockMvcRequestBuilders.get("/report")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertEquals(0, list.size());

        // delete tasks
        taskManagerService.deleteTask(task1.getId());
        taskManagerService.deleteTask(task3.getId());
        actions = mockMvc.perform(MockMvcRequestBuilders.get("/report")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertEquals(0, list.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetByTask() throws Exception {
        mockAuthentication();

        Task task = new Task();
        Date start = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 12);
        task.setScheduledstart(start);
        task.setStart(start);
        Date scheduledEnd = new Date();
        Date end = new Date(System.currentTimeMillis() - 1000L * 60 * 10); // 10 min
        task.setScheduledEnd(scheduledEnd);
        task.setEnd(end);
        task.setName(UUID.randomUUID().toString());
        task.setUserName(USERNAME);
        taskRepository.save(task);
        reportService.generateReport(task);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/report/task/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(content, Map.class);
        assertEquals(TaskReportType.EARLIER.name(), map.get("type"));
        assertEquals(task.getId(), ((Number) map.get("taskId")).longValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetByTaskWithWrongUsername() throws Exception {
        String username = UUID.randomUUID().toString();
        mockAuthentication(username);
        Task task = new Task();
        Date start = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 12);
        task.setScheduledstart(start);
        task.setStart(start);
        Date scheduledEnd = new Date();
        Date end = new Date(System.currentTimeMillis() - 1000L * 60 * 10); // 10 min
        task.setScheduledEnd(scheduledEnd);
        task.setEnd(end);
        task.setName(UUID.randomUUID().toString());
        task.setUserName(USERNAME);
        taskRepository.save(task);
        reportService.generateReport(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/report/task/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }
}
