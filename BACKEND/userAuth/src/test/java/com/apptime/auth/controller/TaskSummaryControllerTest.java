package com.apptime.auth.controller;

import com.apptime.auth.model.AllUserTaskSummary;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.UserTaskSummary;
import com.apptime.auth.model.to.SummaryType;
import com.apptime.auth.repository.AllUserTaskSummaryRepository;
import com.apptime.auth.repository.TaskCategoryRepository;
import com.apptime.auth.repository.UserTaskSummaryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Qi Zhang
 * This is the unit test class for TaskSummaryController
 * Use Case: TMGP4-319, TMGP4-320
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskSummaryControllerTest extends AbstractControllerTest {

    @Autowired
    private TaskCategoryRepository categoryRepository;

    @Autowired
    private UserTaskSummaryRepository userTaskSummaryRepository;

    @Autowired
    private AllUserTaskSummaryRepository allUserTaskSummaryRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        cleanup();

        initMvc();
        mockAuthentication();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetUserSummaries() throws Exception {
        TaskCategory category1 = new TaskCategory("c1", USERNAME, false);
        categoryRepository.save(category1);
        UserTaskSummary summary1 = new UserTaskSummary();
        summary1.setUsername(USERNAME);
        summary1.setCategoryId(category1.getId());
        summary1.setAverageDuration(Duration.ofHours(1));
        userTaskSummaryRepository.save(summary1);

        TaskCategory category2 = new TaskCategory("c2", USERNAME, true);
        categoryRepository.save(category2);
        UserTaskSummary summary2 = new UserTaskSummary();
        summary2.setUsername(USERNAME);
        summary2.setCategoryId(category2.getId());
        summary2.setAverageDuration(Duration.ofHours(2));
        userTaskSummaryRepository.save(summary2);

        UserTaskSummary summary3 = new UserTaskSummary();
        summary3.setUsername(UUID.randomUUID().toString());
        summary3.setCategoryId(category2.getId());
        summary3.setAverageDuration(Duration.ofMinutes(30));
        userTaskSummaryRepository.save(summary3);

        AllUserTaskSummary allUserTaskSummary1 = new AllUserTaskSummary();
        allUserTaskSummary1.setCategoryId(category2.getId());
        allUserTaskSummary1.setAverageDuration(Duration.ofMinutes(45));
        allUserTaskSummaryRepository.save(allUserTaskSummary1);

        TaskCategory category3 = new TaskCategory("c3", UUID.randomUUID().toString(), true);
        categoryRepository.save(category3);
        AllUserTaskSummary allUserTaskSummary2 = new AllUserTaskSummary();
        allUserTaskSummary2.setCategoryId(category3.getId());
        allUserTaskSummary2.setAverageDuration(Duration.ofMinutes(35));
        allUserTaskSummaryRepository.save(allUserTaskSummary2);


        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/summary/mine")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = mapper.readValue(content, List.class);
        assertEquals(2, list.size());
        for (Map<String, Object> map : list) {
            Map<String, Object> category = (Map<String, Object>) map.get("category");
            String categoryName = (String) category.get("name");
            Boolean isPublic = (Boolean) category.get("public");

            Map<String, Object> userSummary = (Map<String, Object>) map.get("summaryForCurrentUser");
            Map<String, Object> allUserSummary = (Map<String, Object>) map.get("summaryForAllUsers");

            if (category1.getName().equals(categoryName)) {
                assertFalse(isPublic);
                assertNotNull(userSummary);
                assertNull(allUserSummary);

                String type = (String) userSummary.get("type");
                assertEquals(SummaryType.MINE.name(), type);
                assertTrue(userSummary.containsKey("category"));
                assertTrue(userSummary.containsKey("averageDuration"));
            } else if (category2.getName().equals(categoryName)) {
                assertTrue(isPublic);
                assertNotNull(userSummary);
                assertNotNull(allUserSummary);

                String type = (String) userSummary.get("type");
                assertEquals(SummaryType.MINE.name(), type);
                assertTrue(userSummary.containsKey("category"));
                assertTrue(userSummary.containsKey("averageDuration"));

                String allUserSummaryType = (String) allUserSummary.get("type");
                assertEquals(SummaryType.ALL_USERS.name(), allUserSummaryType);
                assertTrue(allUserSummary.containsKey("category"));
                assertTrue(allUserSummary.containsKey("averageDuration"));
            } else {
                fail();
            }
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetSummariesByCategory() throws Exception {
        TaskCategory category1 = new TaskCategory("c1", USERNAME, false);
        categoryRepository.save(category1);
        UserTaskSummary summary1 = new UserTaskSummary();
        summary1.setUsername(USERNAME);
        summary1.setCategoryId(category1.getId());
        summary1.setAverageDuration(Duration.ofHours(1));
        userTaskSummaryRepository.save(summary1);

        TaskCategory category2 = new TaskCategory("c2", USERNAME, true);
        categoryRepository.save(category2);
        UserTaskSummary summary2 = new UserTaskSummary();
        summary2.setUsername(USERNAME);
        summary2.setCategoryId(category2.getId());
        summary2.setAverageDuration(Duration.ofHours(2));
        userTaskSummaryRepository.save(summary2);

        AllUserTaskSummary allUserTaskSummary = new AllUserTaskSummary();
        allUserTaskSummary.setCategoryId(category2.getId());
        allUserTaskSummary.setAverageDuration(Duration.ofMinutes(20));
        allUserTaskSummaryRepository.save(allUserTaskSummary);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/summary/category/" + category1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(content, Map.class);
        assertTrue(map.containsKey("category"));
        Map<String, Object> category = (Map<String, Object>) map.get("category");
        assertEquals(category1.getName(), category.get("name"));
        assertFalse((Boolean) category.get("public"));
        assertTrue(map.get("summaryForCurrentUser") != null);
        assertTrue(map.get("summaryForAllUsers") == null);

        actions = mockMvc.perform(MockMvcRequestBuilders.get("/summary/category/" + category2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        map = mapper.readValue(content, Map.class);
        assertTrue(map.containsKey("category"));
        category = (Map<String, Object>) map.get("category");
        assertEquals(category2.getName(), category.get("name"));
        assertTrue((Boolean) category.get("public"));
        assertTrue(map.get("summaryForCurrentUser") != null);
        assertTrue(map.get("summaryForAllUsers") != null);
    }
}
