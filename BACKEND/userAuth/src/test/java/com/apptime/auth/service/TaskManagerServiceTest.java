package com.apptime.auth.service;
import com.apptime.auth.config.TaskStateMachine;
import com.apptime.auth.model.FormatedDate;
import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.TaskState;
import com.apptime.auth.repository.TaskCategoryRepository;
import com.apptime.auth.repository.TaskReportRepository;
import com.apptime.auth.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.apptime.auth.model.TaskState.ACTIVE;
import static com.apptime.auth.model.TaskState.COMPLETED;
import static com.apptime.auth.model.TaskState.CREATED;
import static com.apptime.auth.model.TaskState.PAUSED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Autowired
    private TaskCategoryRepository categoryRepository;

    @Autowired
    private TaskReportRepository reportRepository;

    private NotificationService notificationService;

    private TaskReportService reportService;

    @BeforeEach
    public void init() {
        repository.deleteAll();
        categoryRepository.deleteAll();
        reportRepository.deleteAll();
        notificationService = mock(NotificationService.class);
        service.setNotificationService(notificationService);
        reportService = mock(TaskReportService.class);
        service.setReportService(reportService);
        service.setTaskRepo(repository);
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
        assertEquals(TaskState.CREATED, task.getState());
        assertTrue(savedTask.getCategories() == null || savedTask.getCategories().isEmpty());
        verify(notificationService, times(1)).createNotificationForTask(any(Task.class));

        String adminUser = UUID.randomUUID().toString();
        TaskCategory publicCategory = new TaskCategory("public", adminUser, true);
        categoryRepository.save(publicCategory);

        TaskCategory mineCategory = new TaskCategory("mine", username, false);
        categoryRepository.save(mineCategory);

        TaskCategory wrongCategory = new TaskCategory("wrong", UUID.randomUUID().toString(), false);
        categoryRepository.save(wrongCategory);

        Task task2 = new Task();
        String name2 = UUID.randomUUID().toString();
        task2.setName(name2);

        TaskCategory c1 = new TaskCategory();
        c1.setId(publicCategory.getId());
        task2.addCategory(c1);
        TaskCategory c2 = new TaskCategory();
        c2.setName(mineCategory.getName());
        task2.addCategory(c2);
        TaskCategory c3 = new TaskCategory();
        c3.setName(wrongCategory.getName());
        task2.addCategory(c3);
        TaskCategory c4 = new TaskCategory();
        c4.setName(UUID.randomUUID().toString());
        task2.addCategory(c4); // nonexistent category


        Task savedTask2 = service.createTask(task2, username);
        assertEquals(name2, savedTask2.getName());
        assertEquals(username, savedTask2.getUserName());

        assertEquals(2, savedTask2.getCategories().size());
        assertTrue(savedTask2.getCategories().contains(publicCategory));
        assertTrue(savedTask2.getCategories().contains(mineCategory));

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
        String username = "username";

        String adminUser = UUID.randomUUID().toString();
        TaskCategory publicCategory = new TaskCategory("public", adminUser, true);
        categoryRepository.save(publicCategory);

        TaskCategory mineCategory = new TaskCategory("mine", username, false);
        categoryRepository.save(mineCategory);

        Task task = new Task();
        String name = UUID.randomUUID().toString();
        task.setName(name);

        TaskCategory c1 = new TaskCategory();
        c1.setId(publicCategory.getId());
        task.addCategory(c1);
        TaskCategory c2 = new TaskCategory();
        c2.setName(mineCategory.getName());
        task.addCategory(c2);

        Task savedTask = service.createTask(task, username);

        Task taskInDb = service.getTask(savedTask.getId());
        assertNotNull(taskInDb);
        assertEquals(savedTask.getId(), taskInDb.getId());
        assertEquals(2, taskInDb.getCategories().size());

        assertEquals(2, categoryRepository.findAll().size());

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

    @Test
    public void testStart() {
        TaskRepository repository = mock(TaskRepository.class);
        service.setTaskRepo(repository);

        when(repository.findById(anyLong())).thenReturn(null);
        TaskState state = service.start(1L);
        assertNull(state);
        verify(repository, never()).save(any(Task.class));

        Task task = new Task();
        when(repository.findById(anyLong())).thenReturn(task);

        task.setState(CREATED);
        state = service.start(1L);
        assertEquals(ACTIVE, state);
        assertEquals(ACTIVE, task.getState());
        assertNotNull(task.getStart());
        verify(repository, times(1)).save(eq(task));
        clearInvocations(repository);

        task.setState(COMPLETED);
        state = service.start(1L);
        assertEquals(COMPLETED, state);
        assertEquals(COMPLETED, task.getState());
        verify(repository, times(1)).save(eq(task));
    }

    @Test
    public void testgetTasksStartedLaterThan() throws ParseException {
        TaskRepository repository = mock(TaskRepository.class);
        service.setTaskRepo(repository);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sDate="2020-04-22";
        Date start=dateFormat.parse(sDate);
        String name = "userName";
        Task task = new Task();
        Set<Task> tasks = new HashSet<Task>();
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, 1);
        Date end = c.getTime();
                //test empty result
        when(repository.getTasksStartedLaterThan(start,end,name)).thenReturn(tasks);
        Set<Task> resultSet =  service.getTasksStartedLaterThan(start,name);
        assertEquals(0, resultSet.size());
        tasks.add(task);
        task.setScheduledstart(start);
        when(repository.getTasksStartedLaterThan(start,start,name)).thenReturn(tasks);
        Set<Task> result =  service.getTasksStartedLaterThan(start,name);
        assertEquals(1,result.size());
        assertEquals(task.getScheduledstart(),result.iterator().next().getScheduledstart());


    }


    @Test
    public void testPause() {
        TaskRepository repository = mock(TaskRepository.class);
        service.setTaskRepo(repository);

        when(repository.findById(anyLong())).thenReturn(null);
        TaskState state = service.pause(1L);
        assertNull(state);
        verify(repository, never()).save(any(Task.class));

        Task task = new Task();
        when(repository.findById(anyLong())).thenReturn(task);

        task.setState(ACTIVE);
        task.setStart(new Date(System.currentTimeMillis() - 1000L));
        state = service.pause(1L);
        assertEquals(PAUSED, state);
        assertEquals(PAUSED, task.getState());
        assertNotNull(task.getDuration());
        verify(repository, times(1)).save(eq(task));
        clearInvocations(repository);

        task.setState(COMPLETED);
        state = service.pause(1L);
        assertEquals(COMPLETED, state);
        verify(repository, times(1)).save(eq(task));
    }

    @Test
    public void testComplete() {
        TaskRepository repository = mock(TaskRepository.class);
        service.setTaskRepo(repository);

        when(repository.findById(anyLong())).thenReturn(null);
        TaskState state = service.complete(1L);
        assertNull(state);
        verify(repository, never()).save(any(Task.class));

        Task task = new Task();
        when(repository.findById(anyLong())).thenReturn(task);

        task.setState(ACTIVE);
        task.setStart(new Date(System.currentTimeMillis() - 1000L));
        state = service.complete(1L);
        assertEquals(COMPLETED, state);
        assertEquals(COMPLETED, task.getState());
        assertNotNull(task.getDuration());
        assertNotNull(task.getEnd());
        verify(repository, times(1)).save(eq(task));
        verify(reportService, times(1)).generateReport(any());
        clearInvocations(repository, reportService);

        task.setState(CREATED);
        state = service.complete(1L);
        assertEquals(CREATED, state);
        verify(repository, times(1)).save(eq(task));
        verify(reportService, times(1)).generateReport(any());
    }
}
