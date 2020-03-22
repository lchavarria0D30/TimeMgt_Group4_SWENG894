package com.apptime.auth.service;

import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskReport;
import com.apptime.auth.model.TaskReportType;
import com.apptime.auth.repository.TaskReportRepository;
import com.apptime.auth.repository.TaskRepository;
import com.apptime.auth.service.impl.TaskReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author Qi Zhang
 * This is the unit test class for TaskReportService
 * Use Case: TMGP4-26, TMGP4-31
 */
@SpringBootTest
public class TaskReportServiceImplTest {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskReportRepository reportRepository;

    @Autowired
    private TaskReportServiceImpl service;

    @BeforeEach
    public void init() {
        reportRepository.deleteAll();
        taskRepository.deleteAll();
        service.setReportRepository(reportRepository);
    }

    @Test
    public void testGenerateReportWithInvalidTask() {
        TaskReportRepository reportRepository = mock(TaskReportRepository.class);
        service.setReportRepository(reportRepository);
        assertNull(service.generateReport(null));
        verify(reportRepository, never()).findByTask(any());

        Task task = new Task();
        task.setEnd(null);
        task.setScheduledEnd(null);
        assertNull(service.generateReport(task));
        verify(reportRepository, never()).findByTask(any());

        task.setEnd(new Date());
        task.setScheduledEnd(null);
        assertNull(service.generateReport(task));
        verify(reportRepository, never()).findByTask(any());

        task.setEnd(null);
        task.setScheduledEnd(new Date());
        assertNull(service.generateReport(task));
        verify(reportRepository, never()).findByTask(any());
    }

    @Test
    public void testGenerateReportEndingLater() {
        String username = UUID.randomUUID().toString();
        Task task = new Task();
        task.setUserName(username);
        task.setName("name");

        long current = System.currentTimeMillis();
        long gap = 1000L * 60 * 20; // 20 minutes
        Date scheduledEnd = new Date(current - gap); // 20 minutes ago
        Date actualEnd = new Date(current);
        task.setScheduledEnd(scheduledEnd);
        task.setEnd(actualEnd);
        taskRepository.save(task);

        TaskReport report = service.generateReport(task);
        assertNotNull(report);
        assertEquals(TaskReportType.LATER, report.getType());
        assertEquals(gap, report.getDifference().toMillis());
    }

    @Test
    public void testGenerateReportEndingEarlier() {
        String username = UUID.randomUUID().toString();
        Task task = new Task();
        task.setUserName(username);
        task.setName("name");

        long current = System.currentTimeMillis();
        long gap = 1000L * 60 * 20; // 20 minutes
        Date scheduledEnd = new Date(current + gap); // 20 minutes later
        Date actualEnd = new Date(current);
        task.setScheduledEnd(scheduledEnd);
        task.setEnd(actualEnd);
        taskRepository.save(task);

        TaskReport report = service.generateReport(task);
        assertNotNull(report);
        assertEquals(TaskReportType.EARLIER, report.getType());
        assertEquals(gap, report.getDifference().toMillis());
    }

    @Test
    public void testFindByOwner() {
        String username = UUID.randomUUID().toString();
        Task task = new Task();
        task.setUserName(username);
        task.setName("name");

        long current = System.currentTimeMillis();
        long gap = 1000L * 60 * 20; // 20 minutes
        Date scheduledEnd = new Date(current + gap); // 20 minutes later
        Date actualEnd = new Date(current);
        task.setScheduledEnd(scheduledEnd);
        task.setEnd(actualEnd);
        taskRepository.save(task);

        TaskReport report = new TaskReport();
        report.setType(TaskReportType.EARLIER);
        report.setTask(task);
        report.setDifference(Duration.ofMillis(gap));
        reportRepository.save(report);

        List<TaskReport> reportList = service.getReports(username);
        assertNotNull(reportList);
        assertEquals(1, reportList.size());

        // use different username
        reportList = service.getReports(UUID.randomUUID().toString());
        assertNotNull(reportList);
        assertTrue(reportList.isEmpty());

        // use null username
        reportList = service.getReports(null);
        assertNotNull(reportList);
        assertTrue(reportList.isEmpty());

        // use empty username
        reportList = service.getReports("");
        assertNotNull(reportList);
        assertTrue(reportList.isEmpty());
    }
}
