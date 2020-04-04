package com.apptime.auth.service;

import com.apptime.auth.model.Notification;
import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskReport;

import java.util.List;

/**
 * @author Qi Zhang
 * Service Interface for Notification
 * Use Case: TMGP4-40, TMGP4-38
 */
public interface NotificationService {
    String TYPE_FOR_TASK = "task";
    String TYPE_FOR_EXCEEDED_TASK = "exceeded_task";
    String TYPE_FOR_TASK_REPORT = "task_report";

    String CONTENT_PATTERN_FOR_TASK = "The task (%s) should be started at %s.";
    String CONTENT_PATTERN_FOR_EXCEEDED_TASK = "The task (%s) is overdue. It should be done at %s.";

    String CONTENT_PATTERN_FOR_TASK_REPORT_ON_TIME = "The task (%s) is done on time.";
    String CONTENT_PATTERN_FOR_TASK_REPORT_EARLIER = "The task (%s) is done %s before the scheduled end time.";
    String CONTENT_PATTERN_FOR_TASK_REPORT_LATER = "The task (%s) is done %s after the scheduled end time.";

    long SNOOZE_TIME_IN_MIL_SEC = (long) 1000 * 60 * 5;
    long REMIND_TIME_BEFORE_START_IN_MIL_SEC = (long) 1000 * 60 * 10;
    long REMIND_TIME_AFTER_ESTIMATED_END_IN_MIL_SEC = (long) 1000 * 60 * 5;

    List<Notification> retrieveUndeliveredNotifications(String owner);
    boolean snoozeNotification(int id, String owner);
    boolean deleteNotification(int id, String owner);

    boolean createNotificationForTask(Task task);
    boolean updateNotificationForTask(Task task);
    boolean deleteNotificationForTask(Task task);

    boolean createNotificationForTaskReport(TaskReport report);
}
