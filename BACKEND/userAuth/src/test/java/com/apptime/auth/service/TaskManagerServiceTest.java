package com.apptime.auth.service;

import com.apptime.auth.model.Task;
import com.apptime.auth.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Qi Zhang
 * The unit test class for TaskManagerServiceTest
 */
@SpringBootTest
public class TaskManagerServiceTest {
    @Autowired
    private TaskManagerService service;

    @Autowired
    private TaskRepository repository;

    private NotificationService notificationService;

    @BeforeEach
    public void init() {
        repository.deleteAll();
        notificationService = mock(NotificationService.class);
        service.setNotificationService(notificationService);
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        String name = UUID.randomUUID().toString();
        task.setName(name);
        String username = "username";
        Task savedTask = service.createTask(task, username);

        assertEquals(name, savedTask.getName());
        assertEquals(username, savedTask.getUserName());

        verify(notificationService, times(1)).createNotificationForTask(any(Task.class));

        Task task2 = new Task();
        String name2 = UUID.randomUUID().toString();
        task2.setName(name2);
        Task savedTask2 = service.createTask(task2, username);
        assertEquals(name2, savedTask2.getName());
        assertEquals(username, savedTask2.getUserName());

        assertNotEquals(savedTask.getId(), savedTask2.getId());

        Task taskInDb1 = service.getTask(savedTask.getId());
        assertNotNull(taskInDb1);
        assertEquals(savedTask.getId(), taskInDb1.getId());
        assertEquals(name, taskInDb1.getName());
        assertEquals(username, taskInDb1.getUserName());

        Task taskInDb2 = service.getTask(savedTask2.getId());
        assertNotNull(taskInDb2);
        assertEquals(savedTask2.getId(), taskInDb2.getId());
        assertEquals(name2, taskInDb2.getName());
        assertEquals(username, taskInDb2.getUserName());
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task();
        String name = UUID.randomUUID().toString();
        task.setName(name);
        String username = "username";
        Task savedTask = service.createTask(task, username);

        String newName = UUID.randomUUID().toString();
        savedTask.setName(newName);
        String desc = UUID.randomUUID().toString();
        assertNotEquals(desc, savedTask.getDescription());
        savedTask.setDescription(desc);
        Task updatedTask = service.updateTask(savedTask, username);
        assertEquals(savedTask.getId(), updatedTask.getId());
        assertEquals(savedTask.getUserName(), updatedTask.getUserName());
        assertNotEquals(name, updatedTask.getName());
        assertEquals(newName, updatedTask.getName());
        assertEquals(desc, updatedTask.getDescription());

        verify(notificationService, times(1)).updateNotificationForTask(any(Task.class));

        // try update with different username
        assertNull(service.updateTask(updatedTask, UUID.randomUUID().toString()));

        repository.deleteAll();
        updatedTask = service.updateTask(savedTask, username);
        assertNull(updatedTask);
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task();
        String name = UUID.randomUUID().toString();
        task.setName(name);
        String username = "username";
        Task savedTask = service.createTask(task, username);

        Task taskInDb = service.getTask(savedTask.getId());
        assertNotNull(taskInDb);
        assertEquals(savedTask.getId(), taskInDb.getId());

        Task deletedTask = service.deleteTask(taskInDb.getId());
        assertNotNull(deletedTask);
        assertEquals(deletedTask.getId(), taskInDb.getId());

        verify(notificationService, times(1)).deleteNotificationForTask(any(Task.class));

        deletedTask = service.deleteTask(taskInDb.getId());
        assertNull(deletedTask);
    }

    @Test
    public void testGetTask() {
        Task task = new Task();
        String name = UUID.randomUUID().toString();
        task.setName(name);
        String username = "username";
        Task savedTask = service.createTask(task, username);

        Task taskInDb = service.getTask(savedTask.getId());
        assertNotNull(taskInDb);
        assertEquals(savedTask.getId(), taskInDb.getId());

        repository.deleteAll();
        taskInDb = service.getTask(savedTask.getId());
        assertNull(taskInDb);
    }
}
