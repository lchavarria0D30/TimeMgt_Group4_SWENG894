package com.apptime.auth.service;

import com.apptime.auth.model.Notification;
import com.apptime.auth.model.Task;
import com.apptime.auth.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.apptime.auth.service.NotificationService.CONTENT_PATTERN_FOR_EXCEEDED_TASK;
import static com.apptime.auth.service.NotificationService.CONTENT_PATTERN_FOR_TASK;
import static com.apptime.auth.service.NotificationService.REMIND_TIME_BEFORE_START_IN_MIL_SEC;
import static com.apptime.auth.service.NotificationService.TYPE_FOR_EXCEEDED_TASK;
import static com.apptime.auth.service.NotificationService.TYPE_FOR_TASK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Qi Zhang
 * Unit Test for NotificationService
 * Use Case: TMGP4-40, TMGP4-38
 */
@SpringBootTest
public class NotificationServiceTest {
    @Autowired
    private NotificationService service;

    @Autowired
    private NotificationRepository repository;

    @BeforeEach
    public void init() {
        repository.deleteAll();
    }

    @Test
    public void testRetrieveUndeliveredNotifications() {
        final String username = "username";
        Notification n1 = new Notification(username, "t1", "k1", "c1", new Date(System.currentTimeMillis() - 1000L));
        repository.save(n1);
        Notification n2 = new Notification(username, "t1", "k2", "c2", new Date(System.currentTimeMillis() - 10000L));
        repository.save(n2);
        Notification n3 = new Notification(username, "t2", "k3", "c3", new Date(System.currentTimeMillis() + 10000L));
        repository.save(n3);
        Notification n4 = new Notification(username, "t2", "k4", "c4", new Date(System.currentTimeMillis() - 5000L));
        repository.save(n4);
        List<Notification> notifications = service.retrieveUndeliveredNotifications(username);
        assertNotNull(notifications);
        assertFalse(notifications.isEmpty());
        assertEquals(3, notifications.size());
        for (Notification n : notifications) {
            assertTrue(n.isDelivered());
        }

        String randomUsername = UUID.randomUUID().toString();
        notifications = service.retrieveUndeliveredNotifications(randomUsername);
        assertTrue(notifications.isEmpty());
    }

    @Test
    public void testSnoozeNotification() {
        final String username = "username";
        Notification n1 = new Notification(username, "t1", "k1", "c1", new Date(System.currentTimeMillis() - 1000L));
        repository.save(n1);

        List<Notification> notifications = service.retrieveUndeliveredNotifications(username);
        assertNotNull(notifications);
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());

        Notification notification = notifications.iterator().next();
        assertTrue(notification.isDelivered());
        assertTrue(notification.getRemindTime().getTime() <= System.currentTimeMillis());

        boolean b = service.snoozeNotification(notification.getId(), username);
        assertTrue(b);
        notifications = service.retrieveUndeliveredNotifications(username);
        assertTrue(notifications.isEmpty());

        Optional<Notification> notificationOptional = repository.findById(notification.getId());
        assertTrue(notificationOptional.isPresent());
        Notification snoozedNotification = notificationOptional.get();
        assertEquals(notification.getId(), snoozedNotification.getId());
        assertNotEquals(notification.getRemindTime(), snoozedNotification.getRemindTime());
        assertTrue(snoozedNotification.getRemindTime().getTime() > System.currentTimeMillis());
        Date newRemindTime = snoozedNotification.getRemindTime();

        String randomUsername = UUID.randomUUID().toString();
        b = service.snoozeNotification(notification.getId(), randomUsername);
        assertFalse(b);

        notificationOptional = repository.findById(notification.getId());
        assertTrue(notificationOptional.isPresent());
        snoozedNotification = notificationOptional.get();
        assertEquals(notification.getId(), snoozedNotification.getId());
        assertEquals(newRemindTime, snoozedNotification.getRemindTime());
    }

    @Test
    public void testDeleteNotification() {
        final String username = "username";
        Notification n1 = new Notification(username, "t1", "k1", "c1", new Date(System.currentTimeMillis() - 1000L));
        repository.save(n1);

        List<Notification> notifications = service.retrieveUndeliveredNotifications(username);
        assertNotNull(notifications);
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());

        Notification notification = notifications.iterator().next();
        assertTrue(notification.isDelivered());
        assertTrue(notification.getRemindTime().getTime() <= System.currentTimeMillis());


        String randomUsername = UUID.randomUUID().toString();
        boolean b = service.deleteNotification(notification.getId(), randomUsername);
        assertFalse(b);

        Optional<Notification> notificationOptional = repository.findById(notification.getId());
        assertTrue(notificationOptional.isPresent());
        Notification notificationInDb = notificationOptional.get();
        assertEquals(notification.getId(), notificationInDb.getId());
        assertEquals(notification.getRemindTime(), notificationInDb.getRemindTime());

        b = service.deleteNotification(notification.getId(), username);
        assertTrue(b);
        notifications = service.retrieveUndeliveredNotifications(username);
        assertTrue(notifications.isEmpty());

        notificationOptional = repository.findById(notification.getId());
        assertFalse(notificationOptional.isPresent());
    }

    @Test
    public void testCreateNotificationForTask() {
        assertFalse(service.createNotificationForTask(null));

        Task task = new Task();
        assertFalse(service.createNotificationForTask(task));

        String username = UUID.randomUUID().toString();
        task.setUserName(username);
        assertFalse((service.createNotificationForTask(task)));

        Date startTime = new Date(System.currentTimeMillis() + (long) 1000 * 60 * 60 * 24); // one day later
        task.setScheduledstart(startTime);

        Random r = new Random();
        long id = r.nextLong();
        task.setId(id);

        String taskName = "taskName";
        task.setName(taskName);

        assertTrue(service.createNotificationForTask(task));

        List<Notification> notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_TASK, Long.toString(id));
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
        Notification notification = notifications.iterator().next();
        Date remindTime = notification.getRemindTime();
        assertEquals(startTime.getTime() - REMIND_TIME_BEFORE_START_IN_MIL_SEC, remindTime.getTime());
        assertEquals(String.format(CONTENT_PATTERN_FOR_TASK, taskName, startTime), notification.getContent());

        // no duration, so no TYPE_FOR_EXCEED_TASK
        notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_EXCEEDED_TASK, Long.toString(id));
        assertTrue(notifications.isEmpty());

        // create notification for the task again (with duration)
        startTime = new Date(System.currentTimeMillis() + (long) 1000 * 60 * 60 * 24 * 2); // two day later
        task.setScheduledstart(startTime);

        Date endTime = new Date(startTime.getTime() + (long) 1000 * 60 * 60); // one hour
        LocalDateTime duration = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());
        task.setDuration(duration);

        assertTrue(service.createNotificationForTask(task));

        notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_TASK, Long.toString(id));
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
        notification = notifications.iterator().next();
        remindTime = notification.getRemindTime();
        assertEquals(startTime.getTime() - REMIND_TIME_BEFORE_START_IN_MIL_SEC, remindTime.getTime());
        assertEquals(String.format(CONTENT_PATTERN_FOR_TASK, taskName, startTime), notification.getContent());

        notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_EXCEEDED_TASK, Long.toString(id));
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
        notification = notifications.iterator().next();
        remindTime = notification.getRemindTime();
        assertEquals(endTime.getTime() + (long) 1000 * 60 * 5, remindTime.getTime()); // five minute later
        assertEquals(String.format(CONTENT_PATTERN_FOR_EXCEEDED_TASK, taskName, duration), notification.getContent());
    }

    @Test
    public void testUpdateNotificationForTask() {
        assertFalse(service.updateNotificationForTask(null));

        Task task = new Task();
        assertFalse(service.updateNotificationForTask(task));

        String username = UUID.randomUUID().toString();
        task.setUserName(username);
        assertFalse((service.updateNotificationForTask(task)));

        Date startTime = new Date(System.currentTimeMillis() + (long) 1000 * 60 * 60 * 24); // one day later
        task.setScheduledstart(startTime);

        Random r = new Random();
        long id = r.nextLong();
        task.setId(id);

        String taskName = "taskName";
        task.setName(taskName);

        assertTrue(service.updateNotificationForTask(task));

        List<Notification> notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_TASK, Long.toString(id));
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
        Notification notification = notifications.iterator().next();
        Date remindTime = notification.getRemindTime();
        assertEquals(startTime.getTime() - REMIND_TIME_BEFORE_START_IN_MIL_SEC, remindTime.getTime());
        assertEquals(String.format(CONTENT_PATTERN_FOR_TASK, taskName, startTime), notification.getContent());

        // no duration, so no TYPE_FOR_EXCEED_TASK
        notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_EXCEEDED_TASK, Long.toString(id));
        assertTrue(notifications.isEmpty());

        // create notification for the task again
        startTime = new Date(System.currentTimeMillis() + (long) 1000 * 60 * 60 * 24 * 2); // two day later
        task.setScheduledstart(startTime);

        Date endTime = new Date(startTime.getTime() + (long) 1000 * 60 * 60); // one hour
        LocalDateTime duration = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());
        task.setDuration(duration);

        assertTrue(service.updateNotificationForTask(task));

        notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_TASK, Long.toString(id));
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
        notification = notifications.iterator().next();
        remindTime = notification.getRemindTime();
        assertEquals(startTime.getTime() - REMIND_TIME_BEFORE_START_IN_MIL_SEC, remindTime.getTime());
        assertEquals(String.format(CONTENT_PATTERN_FOR_TASK, taskName, startTime), notification.getContent());

        notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_EXCEEDED_TASK, Long.toString(id));
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
        notification = notifications.iterator().next();
        remindTime = notification.getRemindTime();
        assertEquals(endTime.getTime() + (long) 1000 * 60 * 5, remindTime.getTime()); // five minute later
        assertEquals(String.format(CONTENT_PATTERN_FOR_EXCEEDED_TASK, taskName, duration), notification.getContent());
    }

    @Test
    public void testDeleteNotificationForTask() {
        assertFalse(service.deleteNotificationForTask(null));

        Task task = new Task();
        assertFalse(service.deleteNotificationForTask(task));

        Random r = new Random();
        long id = r.nextLong();
        task.setId(id);

        String username = UUID.randomUUID().toString();
        task.setUserName(username);
        assertTrue((service.deleteNotificationForTask(task)));

        List<Notification> notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_TASK, Long.toString(id));
        assertTrue(notifications.isEmpty());
        notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_EXCEEDED_TASK, Long.toString(id));
        assertTrue(notifications.isEmpty());

        // create notification firstly, then delete it
        Date startTime = new Date(System.currentTimeMillis() + (long) 1000 * 60 * 60 * 24); // one day later
        task.setScheduledstart(startTime);

        String taskName = "taskName";
        task.setName(taskName);

        Date endTime = new Date(startTime.getTime() + (long) 1000 * 60 * 60); // one hour
        LocalDateTime duration = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());
        task.setDuration(duration);

        assertTrue(service.createNotificationForTask(task));

        notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_TASK, Long.toString(id));
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());

        notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_EXCEEDED_TASK, Long.toString(id));
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());

        assertTrue((service.deleteNotificationForTask(task)));

        notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_TASK, Long.toString(id));
        assertTrue(notifications.isEmpty());
        notifications = repository.findByOwnerTypeKey(username, TYPE_FOR_EXCEEDED_TASK, Long.toString(id));
        assertTrue(notifications.isEmpty());
    }
}
