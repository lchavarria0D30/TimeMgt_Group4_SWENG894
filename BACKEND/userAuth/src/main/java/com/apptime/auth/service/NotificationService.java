package com.apptime.auth.service;

import com.apptime.auth.model.Notification;
import com.apptime.auth.model.Task;

import java.util.List;

/**
 * @author Qi Zhang
 * Service Interface for Notification
 * Use Case: TMGP4-40, TMGP4-38
 */
public interface NotificationService {
    String TYPE_FOR_TASK = "task";
    String CONTENT_PATTERN_FOR_TASK = "The task (%s) should be started at %s.";

    long SNOOZE_TIME_IN_MIL_SEC = (long) 1000 * 60 * 5;
    long REMIND_TIME_BEFORE_START_IN_MIL_SEC = (long) 1000 * 60 * 10;

    List<Notification> retrieveUndeliveredNotifications(String owner);
    boolean snoozeNotification(int id, String owner);
    boolean deleteNotification(int id, String owner);

    boolean createNotificationForTask(Task task);
    boolean updateNotificationForTask(Task task);
    boolean deleteNotificationForTask(Task task);
}
