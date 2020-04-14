package com.apptime.auth.service;

import com.apptime.auth.BaseTest;
import com.apptime.auth.model.AllUserTaskSummary;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.UserTaskSummary;
import com.apptime.auth.repository.AllUserTaskSummaryRepository;
import com.apptime.auth.repository.UserTaskSummaryRepository;
import com.apptime.auth.service.impl.TaskSummaryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Qi Zhang
 * This is the unit test class for TaskSummaryService
 * Use Case: TMGP4-319, TMGP4-320
 */
public class TaskSummaryServiceTest {
    private TaskSummaryServiceImpl service;

    private UserTaskSummaryRepository userTaskSummaryRepository;
    private AllUserTaskSummaryRepository allUserTaskSummaryRepository;

    @BeforeEach
    public void init() {
        userTaskSummaryRepository = mock(UserTaskSummaryRepository.class);
        allUserTaskSummaryRepository = mock(AllUserTaskSummaryRepository.class);

        service = new TaskSummaryServiceImpl();
        service.setUserTaskSummaryRepository(userTaskSummaryRepository);
        service.setAllUserTaskSummaryRepository(allUserTaskSummaryRepository);
    }

    @Test
    public void testGetUserTaskSummaries() {
        List<UserTaskSummary> list = service.getUserTaskSummaries(null);
        assertTrue(list.isEmpty());
        verify(userTaskSummaryRepository, never()).findByUsername(any());

        String username = UUID.randomUUID().toString();
        when(userTaskSummaryRepository.findByUsername(eq(username))).thenReturn(Collections.singletonList(mock(UserTaskSummary.class)));
        list = service.getUserTaskSummaries(username);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        verify(userTaskSummaryRepository, times(1)).findByUsername(eq(username));
    }

    @Test
    public void testGetUserTaskSummaryByCategory() {
        Optional<UserTaskSummary> optional = service.getUserTaskSummaryByCategory(null, null);
        assertFalse(optional.isPresent());
        verify(userTaskSummaryRepository, never()).findByUsernameCategory(any(), anyInt());

        optional = service.getUserTaskSummaryByCategory(null, mock(TaskCategory.class));
        assertFalse(optional.isPresent());
        verify(userTaskSummaryRepository, never()).findByUsernameCategory(any(), anyInt());

        optional = service.getUserTaskSummaryByCategory(UUID.randomUUID().toString(), null);
        assertFalse(optional.isPresent());
        verify(userTaskSummaryRepository, never()).findByUsernameCategory(any(), anyInt());

        List<UserTaskSummary> list = new ArrayList<>();
        UserTaskSummary summary = mock(UserTaskSummary.class);
        list.add(summary);
        when(userTaskSummaryRepository.findByUsernameCategory(any(), anyInt())).thenReturn(list);
        optional = service.getUserTaskSummaryByCategory(UUID.randomUUID().toString(), mock(TaskCategory.class));
        assertTrue(optional.isPresent());
        assertEquals(summary, optional.get());

        UserTaskSummary summary1 = mock(UserTaskSummary.class);
        list.add(summary1);
        when(userTaskSummaryRepository.findByUsernameCategory(any(), anyInt())).thenReturn(list);
        optional = service.getUserTaskSummaryByCategory(UUID.randomUUID().toString(), mock(TaskCategory.class));
        assertTrue(optional.isPresent());
        assertEquals(summary, optional.get());
    }

    @Test
    public void testGetAllUserTaskSummaryByCategory() {
        Optional<AllUserTaskSummary> optional = service.getAllUserTaskSummaryByCategory(null);
        assertFalse(optional.isPresent());
        verify(allUserTaskSummaryRepository, never()).findByCategory(anyInt());

        TaskCategory category = mock(TaskCategory.class);
        when(category.isPublic()).thenReturn(false);
        optional = service.getAllUserTaskSummaryByCategory(category);
        assertFalse(optional.isPresent());
        verify(allUserTaskSummaryRepository, never()).findByCategory(anyInt());

        when(category.isPublic()).thenReturn(true);
        when(allUserTaskSummaryRepository.findByCategory(anyInt())).thenReturn(null);
        optional = service.getAllUserTaskSummaryByCategory(category);
        assertFalse(optional.isPresent());
        verify(allUserTaskSummaryRepository, times(1)).findByCategory(anyInt());

        AllUserTaskSummary summary = mock(AllUserTaskSummary.class);
        when(allUserTaskSummaryRepository.findByCategory(anyInt())).thenReturn(summary);
        optional = service.getAllUserTaskSummaryByCategory(category);
        assertTrue(optional.isPresent());
        assertEquals(summary, optional.get());
    }
}
