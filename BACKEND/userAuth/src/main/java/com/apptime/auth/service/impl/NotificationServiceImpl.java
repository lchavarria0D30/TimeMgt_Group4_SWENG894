package com.apptime.auth.service.impl;

import com.apptime.auth.model.Notification;
import com.apptime.auth.model.Task;
import com.apptime.auth.repository.NotificationRepository;
import com.apptime.auth.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Qi Zhang
 * Service Implementation for Notification
 * Use Case: TMGP4-40, TMGP4-38
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Sort SORT = Sort.by("remindTime").descending();

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    @Transactional
    public List<Notification> retrieveUndeliveredNotifications(String owner) {
        Date now = new Date();
        List<Notification> notifications = notificationRepository.findNewNotifications(owner, now, SORT);
        for (Notification n : notifications) {
            n.setDelivered(true);
            notificationRepository.save(n);
        }
        return notifications;
    }

    @Override
    @Transactional
    public boolean snoozeNotification(int id, String owner) {
        Optional<Notification> notificationOptional = notificationRepository.findById(id);
        return notificationOptional.map(notification -> {
            if (owner.equals(notification.getOwner())) {
                Date newRemindTime = new Date(System.currentTimeMillis() + SNOOZE_TIME_IN_MIL_SEC); // set five minutes later
                notification.setRemindTime(newRemindTime);
                notification.setDelivered(false);
                notificationRepository.save(notification);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean deleteNotification(int id, String owner) {
        Optional<Notification> notificationOptional = notificationRepository.findById(id);
        return notificationOptional.map(notification -> {
            if (owner.equals(notification.getOwner())) {
                notificationRepository.deleteById(id);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean createNotificationForTask(Task task) {
        if (task == null || task.getUserName() == null || task.getScheduledstart() == null) {
            return false;
        }
        clearNotificationsForTask(task);
        String key = Long.toString(task.getId());
        String content = String.format(CONTENT_PATTERN_FOR_TASK, task.getName(), task.getScheduledstart());
        // the notification would be reminded 10 minutes before the start time
        Notification notification = new Notification(task.getUserName(), TYPE_FOR_TASK, key, content, new Date(task.getScheduledstart().getTime() - REMIND_TIME_BEFORE_START_IN_MIL_SEC));
        notificationRepository.save(notification);
        return true;
    }

    @Override
    @Transactional
    public boolean updateNotificationForTask(Task task) {
        return createNotificationForTask(task);
    }

    @Override
    @Transactional
    public boolean deleteNotificationForTask(Task task) {
        if (task == null || task.getUserName() == null) {
            return false;
        }
        clearNotificationsForTask(task);
        return true;
    }

    private void clearNotificationsForTask(Task task) {
        List<Notification> notifications = notificationRepository.findByOwnerTypeKey(task.getUserName(), TYPE_FOR_TASK, Long.toString(task.getId()));
        if (!notifications.isEmpty()) {
            for (Notification notification : notifications) {
                notificationRepository.delete(notification);
            }
        }
    }
}
