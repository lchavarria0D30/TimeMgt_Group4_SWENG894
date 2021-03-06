package com.apptime.auth.service;

import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskReport;

import java.util.List;

/**
 * @author Qi Zhang
 * This is the Service interface for TaskReport
 * Use Case: TMGP4-26, TMGP4-31, TMGP4-35
 */
public interface TaskReportService {
    TaskReport generateReport(Task task);
    List<TaskReport> getReports(String owner);
    List<TaskReport> getReports(String owner, String startDate, String endDate);
    TaskReport findByTaskId(long id);
}
