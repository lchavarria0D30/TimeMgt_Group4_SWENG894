package com.apptime.auth.helper.impl;

import com.apptime.auth.helper.SummaryHelper;
import com.apptime.auth.model.AllUserTaskSummary;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.UserTaskSummary;
import com.apptime.auth.repository.AllUserTaskSummaryRepository;
import com.apptime.auth.repository.UserTaskSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Qi Zhang
 * The concrete class of SummaryHelper to generate task's summary
 * Use Case: TMGP4-319, TMGP4-320
 */
@Service
public class SummaryHelperImpl implements SummaryHelper {
    public static final String USER_SUMMARY_QUERY_STR = "SELECT AVG(t.duration) FROM Task t JOIN t.categories c WHERE t.userName = :username AND c.id = :category_id AND t.duration IS NOT NULL AND t.state='COMPLETED'";

    public static final String ALL_USERS_SUMMARY_QUERY_STR = "SELECT AVG(t.duration) FROM Task t JOIN t.categories c WHERE c.id = :category_id AND c.isPublic IS TRUE AND t.duration IS NOT NULL AND t.state='COMPLETED'";

    @Autowired
    private UserTaskSummaryRepository userTaskSummaryRepository;

    @Autowired
    private AllUserTaskSummaryRepository allUserTaskSummaryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void start(String username, Set<TaskCategory> categories) {
        if (username == null || categories == null || categories.isEmpty()) {
            return;
        }
        ExecutorService executorService = getExecutorService();
        executorService.execute(new UserTaskSummaryRunnable(username, categories, this));
    }

    ExecutorService getExecutorService() {
        return Executors.newCachedThreadPool();
    }

    @Override
    @Transactional
    public void generateSummary(String username, Set<TaskCategory> categories) {
        if (username == null || categories == null || categories.size() == 0
                || entityManager == null || userTaskSummaryRepository == null || allUserTaskSummaryRepository == null) {
            return;
        }

        Query userSummaryQuery = entityManager.createQuery(USER_SUMMARY_QUERY_STR);
        Query allUserSummaryQuery = entityManager.createQuery(ALL_USERS_SUMMARY_QUERY_STR);
        for (TaskCategory category : categories) {
            userSummaryQuery.setParameter("username", username);
            userSummaryQuery.setParameter("category_id", category.getId());
            List<?> resultList = userSummaryQuery.getResultList();
            if (resultList != null && !resultList.isEmpty()) {
                Object result = resultList.iterator().next();
                if (result instanceof Number) {
                    long timeInNano = ((Number) result).longValue();
                    Duration averageDuration = Duration.ofNanos(timeInNano);
                    List<UserTaskSummary> existingSummaries = userTaskSummaryRepository.findByUsernameCategory(username, category.getId());
                    if (existingSummaries != null && !existingSummaries.isEmpty()) {
                        userTaskSummaryRepository.deleteAll(existingSummaries);
                    }
                    UserTaskSummary userTaskSummary = new UserTaskSummary();
                    userTaskSummary.setUsername(username);
                    userTaskSummary.setCategoryId(category.getId());
                    userTaskSummary.setAverageDuration(averageDuration);
                    userTaskSummaryRepository.save(userTaskSummary);
                }
            }
            if (category.isPublic()) {
                // generate all user's summary
                allUserSummaryQuery.setParameter("category_id", category.getId());
                resultList = allUserSummaryQuery.getResultList();
                if (resultList != null && !resultList.isEmpty()) {
                    Object result = resultList.iterator().next();
                    if (result instanceof Number) {
                        long timeInNano = ((Number) result).longValue();
                        Duration averageDurationForAllUser = Duration.ofNanos(timeInNano);
                        AllUserTaskSummary existingSummaries = allUserTaskSummaryRepository.findByCategory(category.getId());
                        if (existingSummaries == null) {
                            existingSummaries = new AllUserTaskSummary();
                            existingSummaries.setCategoryId(category.getId());
                        }
                        existingSummaries.setAverageDuration(averageDurationForAllUser);
                        allUserTaskSummaryRepository.save(existingSummaries);
                    }
                }
            }
        }
    }

    public void setUserTaskSummaryRepository(UserTaskSummaryRepository userTaskSummaryRepository) {
        this.userTaskSummaryRepository = userTaskSummaryRepository;
    }

    public void setAllUserTaskSummaryRepository(AllUserTaskSummaryRepository allUserTaskSummaryRepository) {
        this.allUserTaskSummaryRepository = allUserTaskSummaryRepository;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    static class UserTaskSummaryRunnable implements Runnable {
        private String username;
        private Set<TaskCategory> categories;
        private SummaryHelper helper;

        public UserTaskSummaryRunnable(String username, Set<TaskCategory> categories, SummaryHelper helper) {
            this.username = username;
            this.categories = categories;
            this.helper = helper;
        }

        @Override
        public void run() {
            if (username == null || categories == null || categories.isEmpty() || helper == null) {
                return;
            }
            helper.generateSummary(username, categories);
        }
    }
}
