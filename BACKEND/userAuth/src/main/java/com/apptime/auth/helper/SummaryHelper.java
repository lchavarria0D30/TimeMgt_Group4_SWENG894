package com.apptime.auth.helper;

import com.apptime.auth.model.TaskCategory;

import java.util.Set;

/**
 * @author Qi Zhang
 * The Interface to generate task's summary
 * Use Case: TMGP4-319, TMGP4-320
 */
public interface SummaryHelper {
    void start(String username, Set<TaskCategory> categories);
    void generateSummary(String username, Set<TaskCategory> categories);
}
