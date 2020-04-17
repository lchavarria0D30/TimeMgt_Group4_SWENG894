package com.apptime.auth.service.impl;

import com.apptime.auth.model.AllUserTaskSummary;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.UserTaskSummary;
import com.apptime.auth.repository.AllUserTaskSummaryRepository;
import com.apptime.auth.repository.UserTaskSummaryRepository;
import com.apptime.auth.service.TaskSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Qi Zhang
 * This is the Service implementation for Task Summary
 * Use Case: TMGP4-319, TMGP4-320
 */
@Service
public class TaskSummaryServiceImpl implements TaskSummaryService {
    @Autowired
    private UserTaskSummaryRepository userTaskSummaryRepository;

    @Autowired
    private AllUserTaskSummaryRepository allUserTaskSummaryRepository;

    @Override
    public List<UserTaskSummary> getUserTaskSummaries(String username) {
        if (username == null) {
            return Collections.emptyList();
        }
        return userTaskSummaryRepository.findByUsername(username);
    }

    @Override
    public List<AllUserTaskSummary> getAllUserTaskSummariesByCategories(Collection<TaskCategory> publicCategories) {
        if (publicCategories == null || publicCategories.isEmpty()) {
            return Collections.emptyList();
        }
        return allUserTaskSummaryRepository.findByCategoryIds(publicCategories.stream().map(TaskCategory::getId).collect(Collectors.toList()));
    }

    @Override
    public Optional<UserTaskSummary> getUserTaskSummaryByCategory(String username, TaskCategory category) {
        if (username == null || category == null) {
            return Optional.empty();
        }
        List<UserTaskSummary> summaries = userTaskSummaryRepository.findByUsernameCategory(username, category.getId());
        if (summaries == null || summaries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(summaries.iterator().next());
    }

    @Override
    public Optional<AllUserTaskSummary> getAllUserTaskSummaryByCategory(TaskCategory category) {
        if (category == null || !category.isPublic()) {
            return Optional.empty();
        }
        AllUserTaskSummary summary = allUserTaskSummaryRepository.findByCategory(category.getId());
        return Optional.ofNullable(summary);
    }

    public void setUserTaskSummaryRepository(UserTaskSummaryRepository userTaskSummaryRepository) {
        this.userTaskSummaryRepository = userTaskSummaryRepository;
    }

    public void setAllUserTaskSummaryRepository(AllUserTaskSummaryRepository allUserTaskSummaryRepository) {
        this.allUserTaskSummaryRepository = allUserTaskSummaryRepository;
    }
}
