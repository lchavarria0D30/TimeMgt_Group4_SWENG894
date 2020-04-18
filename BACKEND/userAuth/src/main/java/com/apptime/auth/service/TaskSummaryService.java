package com.apptime.auth.service;

import com.apptime.auth.model.AllUserTaskSummary;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.UserTaskSummary;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Qi Zhang
 * The service interface for Task Summary
 * TMGP4-319: Persistent Object for Average Task Duration by Category for this User (and defining the data model)
 */
public interface TaskSummaryService {
    List<UserTaskSummary> getUserTaskSummaries(String username);
    List<AllUserTaskSummary> getAllUserTaskSummariesByCategories(Collection<TaskCategory> publicCategories);

    Optional<UserTaskSummary> getUserTaskSummaryByCategory(String username, TaskCategory category);
    Optional<AllUserTaskSummary> getAllUserTaskSummaryByCategory(TaskCategory category);
}
