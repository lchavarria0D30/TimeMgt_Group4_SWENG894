package com.apptime.auth.helper;

import com.apptime.auth.BaseTest;
import com.apptime.auth.helper.impl.SummaryHelperImpl;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.repository.AllUserTaskSummaryRepository;
import com.apptime.auth.repository.UserTaskSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Qi Zhang
 * The unit test of SummaryHelper
 * Use Case: TMGP4-319, TMGP4-320
 */
@SpringBootTest
public class SummaryHelperTest extends BaseTest {

    @Autowired
    private SummaryHelperImpl helper;

    private UserTaskSummaryRepository userTaskSummaryRepository;
    private AllUserTaskSummaryRepository allUserTaskSummaryRepository;
    private EntityManager entityManager;

    @BeforeEach
    public void init() {
        userTaskSummaryRepository = mock(UserTaskSummaryRepository.class);
        allUserTaskSummaryRepository = mock(AllUserTaskSummaryRepository.class);
        entityManager = mock(EntityManager.class);

        helper.setUserTaskSummaryRepository(userTaskSummaryRepository);
        helper.setAllUserTaskSummaryRepository(allUserTaskSummaryRepository);
        helper.setEntityManager(entityManager);
    }

    @Test
    public void testGenerateSummary() {
        helper.generateSummary(null, null);
        verify(entityManager, never()).createQuery(anyString());

        helper.generateSummary(UUID.randomUUID().toString(), null);
        verify(entityManager, never()).createQuery(anyString());

        helper.generateSummary(null, Collections.singleton(mock(TaskCategory.class)));
        verify(entityManager, never()).createQuery(anyString());

        helper.generateSummary(UUID.randomUUID().toString(), Collections.emptySet());
        verify(entityManager, never()).createQuery(anyString());

        Query userSummaryQuery = mock(Query.class);
        when(entityManager.createQuery(eq(SummaryHelperImpl.USER_SUMMARY_QUERY_STR))).thenReturn(userSummaryQuery);
        Long userAverageDuration = 1000000L * 1000 * 60 * 60; // one hour
        when(userSummaryQuery.getResultList()).thenReturn(Collections.singletonList(userAverageDuration));

        Query allUserSummaryQuery = mock(Query.class);
        when(entityManager.createQuery(eq(SummaryHelperImpl.ALL_USERS_SUMMARY_QUERY_STR))).thenReturn(allUserSummaryQuery);
        Long allUsersAverageDuration = 1000000L * 1000 * 60 * 60 * 2; // two hours
        when(allUserSummaryQuery.getResultList()).thenReturn(Collections.singletonList(allUsersAverageDuration));

        TaskCategory privateCategory = mock(TaskCategory.class);
        when(privateCategory.isPublic()).thenReturn(false);
        TaskCategory publicCategory = mock(TaskCategory.class);
        when(publicCategory.isPublic()).thenReturn(true);
        Set<TaskCategory> categories = new HashSet<>();
        categories.add(privateCategory);
        categories.add(publicCategory);

        String username = UUID.randomUUID().toString();
        helper.generateSummary(username, categories);

        verify(userTaskSummaryRepository, times(2)).save(any());
        verify(allUserTaskSummaryRepository, times(1)).save(any());
    }
}
