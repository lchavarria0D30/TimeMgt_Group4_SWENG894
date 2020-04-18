package com.apptime.auth.controller;

import com.apptime.auth.model.Notification;
import com.apptime.auth.repository.NotificationRepository;
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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Qi Zhang
 * Unit Test for NotificationController
 * Use Case: TMGP4-40, TMGP4-38
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotificationControllerTest extends AbstractControllerTest {
    private static final String USERNAME = "username";

    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        cleanup();

        initMvc();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRetrieveUndeliveredNotifications() throws Exception {
        mockAuthentication();

        Notification n1 = new Notification(USERNAME, "t1", "k1", "c1", new Date(System.currentTimeMillis() - 1000L));
        notificationRepository.save(n1);
        Notification n2 = new Notification(USERNAME, "t1", "k2", "c2", new Date(System.currentTimeMillis() - 10000L));
        notificationRepository.save(n2);
        // this notification would not be retrieved
        Notification n3 = new Notification(USERNAME, "t2", "k3", "c3", new Date(System.currentTimeMillis() + 10000L));
        notificationRepository.save(n3);
        Notification n4 = new Notification(USERNAME, "t2", "k4", "c4", new Date(System.currentTimeMillis() - 5000L));
        notificationRepository.save(n4);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/notification/new")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = mapper.readValue(content, List.class);
        assertEquals(3, list.size());

        // use other username
        String otherUser = UUID.randomUUID().toString();
        mockAuthentication(otherUser);
        actions = mockMvc.perform(MockMvcRequestBuilders.get("/notification/new")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        list = mapper.readValue(content, List.class);
        assertTrue(list.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSnoozeNotification() throws Exception {
        mockAuthentication();
        Notification n1 = new Notification(USERNAME, "t1", "k1", "c1", new Date(System.currentTimeMillis() - 1000L));
        notificationRepository.save(n1);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/notification/new")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = mapper.readValue(content, List.class);
        assertEquals(1, list.size());

        Map<String, Object> map = list.iterator().next();
        Integer id = (Integer) map.get("id");

        actions = mockMvc.perform(MockMvcRequestBuilders.put("/notification/"+ id + "/snooze")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        assertEquals(Boolean.TRUE.toString(), content);

        // try to use a unknown id
        Random r = new Random();
        int diff = r.nextInt();
        while (diff == 0) {
            diff = r.nextInt();
        }
        int newId = id + diff;
        mockMvc.perform(MockMvcRequestBuilders.put("/notification/"+ newId + "/snooze")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        // try to use a different username to snooze the existing id
        String otherUser = UUID.randomUUID().toString();
        mockAuthentication(otherUser);
        mockMvc.perform(MockMvcRequestBuilders.put("/notification/"+ id + "/snooze")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDeleteNotification() throws Exception {
        mockAuthentication();
        Notification n1 = new Notification(USERNAME, "t1", "k1", "c1", new Date(System.currentTimeMillis() - 1000L));
        notificationRepository.save(n1);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/notification/new")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = mapper.readValue(content, List.class);
        assertEquals(1, list.size());

        Map<String, Object> map = list.iterator().next();
        Integer id = (Integer) map.get("id");

        // use other username to delete it
        String otherUser = UUID.randomUUID().toString();
        mockAuthentication(otherUser);
        mockMvc.perform(MockMvcRequestBuilders.delete("/notification/"+ id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        // use the correct username to delete an unknown id
        mockAuthentication();
        Random r = new Random();
        int diff = r.nextInt();
        while (diff == 0) {
            diff = r.nextInt();
        }
        int newId = id + diff;
        mockMvc.perform(MockMvcRequestBuilders.delete("/notification/"+ newId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        actions = mockMvc.perform(MockMvcRequestBuilders.delete("/notification/"+ id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        assertEquals(Boolean.TRUE.toString(), content);

        // try to delete the same id
        mockMvc.perform(MockMvcRequestBuilders.delete("/notification/"+ id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        // make sure the id is not existing in db
        Optional<Notification> notificationOptional = notificationRepository.findById(id);
        assertFalse(notificationOptional.isPresent());
    }
}
