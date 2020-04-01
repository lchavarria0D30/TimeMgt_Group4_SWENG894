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

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.apptime.auth.service.impl.TaskReportServiceImpl.DATE_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author Qi Zhang
 * This is the unit test class for TaskReportService
 * Use Case: TMGP4-26, TMGP4-31, TMGP4-35
 */
@SpringBootTest
public class TaskReportServiceTest {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskReportRepository reportRepository;

    @Autowired
    private TaskReportServiceImpl service;

    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

    @BeforeEach
    public void init() {
        reportRepository.deleteAll();
        taskRepository.deleteAll();
        service.setReportRepository(reportRepository);
    }

    @Test
    public void testGenerateReportWithInvalidTask() {
        TaskReportRepository mockReportRepository = mock(TaskReportRepository.class);
        service.setReportRepository(mockReportRepository);
        assertNull(service.generateReport(null));

        verify(mockReportRepository, never()).findByTaskId(anyLong());

        Task task = new Task();
        task.setEnd(null);
        task.setScheduledEnd(null);
        assertNull(service.generateReport(task));
        verify(mockReportRepository, never()).findByTaskId(anyLong());

        task.setEnd(new Date());
        task.setScheduledEnd(null);
        assertNull(service.generateReport(task));
        verify(mockReportRepository, never()).findByTaskId(anyLong());

        task.setEnd(null);
        task.setScheduledEnd(new Date());
        assertNull(service.generateReport(task));
        verify(mockReportRepository, never()).findByTaskId(anyLong());
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
        task.setScheduledstart(new Date(current - 1000L * 60 * 120)); // scheduled start time is 120 minutes ago, which means the scheduled duration is 100 minutes
        task.setDuration(Duration.ofMinutes(80)); // the actual duration is 80 minutes

        taskRepository.save(task);

        TaskReport report = service.generateReport(task);
        assertNotNull(report);
        assertEquals(TaskReportType.LATER, report.getType());
        assertEquals(gap, report.getDifference().toMillis());
        assertEquals(Duration.ofMinutes(80), report.getActualDuration());
        assertEquals(Duration.ofMinutes(100), report.getScheduledDuration());
        assertEquals(80, report.getEfficiency());
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
        task.setDuration(Duration.ofMinutes(10));
        taskRepository.save(task);

        TaskReport report = service.generateReport(task);
        assertNotNull(report);
        assertEquals(TaskReportType.EARLIER, report.getType());
        assertEquals(gap, report.getDifference().toMillis());
        assertNotNull(report.getActualDuration());
        assertNull(report.getScheduledDuration());
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
        report.setTaskId(task.getId());
        report.setOwner(username);
        report.setDifference(Duration.ofMillis(gap));
        reportRepository.save(report);

        List<TaskReport> reportList = service.getReports(username);
        assertNotNull(reportList);
        assertEquals(1, reportList.size());

        reportList = service.getReports(username, null, null);
        assertNotNull(reportList);
        assertEquals(1, reportList.size());

        reportList = service.getReports(username, "", "");
        assertNotNull(reportList);
        assertEquals(1, reportList.size());

        // find by start date
        String startDate = dateFormat.format(actualEnd);
        reportList = service.getReports(username, startDate, "");
        assertNotNull(reportList);
        assertEquals(1, reportList.size());

        startDate = dateFormat.format(new Date(actualEnd.getTime() - 1000L * 60 * 60 * 24 * 2));
        reportList = service.getReports(username, startDate, null);
        assertNotNull(reportList);
        assertEquals(1, reportList.size());

        startDate = dateFormat.format(new Date(actualEnd.getTime() + 1000L * 60 * 60 * 24 * 2));
        reportList = service.getReports(username, startDate, "");
        assertNotNull(reportList);
        assertTrue(reportList.isEmpty());

        // find by end date
        String endDate = dateFormat.format(actualEnd);
        reportList = service.getReports(username, null, endDate);
        assertNotNull(reportList);
        assertEquals(1, reportList.size());

        endDate = dateFormat.format(new Date(actualEnd.getTime() + 1000L * 60 * 60 * 24 * 2));
        reportList = service.getReports(username, null, endDate);
        assertNotNull(reportList);
        assertEquals(1, reportList.size());

        endDate = dateFormat.format(new Date(actualEnd.getTime() - 1000L * 60 * 60 * 24 * 2));
        reportList = service.getReports(username, null, endDate);
        assertNotNull(reportList);
        assertTrue(reportList.isEmpty());

        // find by start date and end date
        startDate = dateFormat.format(actualEnd);
        endDate = dateFormat.format(actualEnd);
        reportList = service.getReports(username, startDate, endDate);
        assertNotNull(reportList);
        assertEquals(1, reportList.size());

        endDate = dateFormat.format(new Date(actualEnd.getTime() + 1000L * 60 * 60 * 24 * 2));
        reportList = service.getReports(username, startDate, endDate);
        assertNotNull(reportList);
        assertEquals(1, reportList.size());

        startDate = dateFormat.format(new Date(actualEnd.getTime() - 1000L * 60 * 60 * 24 * 2));
        reportList = service.getReports(username, startDate, endDate);
        assertNotNull(reportList);
        assertEquals(1, reportList.size());

        // wrong range
        reportList = service.getReports(username, endDate, startDate);
        assertNotNull(reportList);
        assertTrue(reportList.isEmpty());

        // end date is earlier
        startDate = dateFormat.format(new Date(actualEnd.getTime() - 1000L * 60 * 60 * 24 * 4));
        endDate = dateFormat.format(new Date(actualEnd.getTime() - 1000L * 60 * 60 * 24 * 2));
        reportList = service.getReports(username, endDate, startDate);
        assertNotNull(reportList);
        assertTrue(reportList.isEmpty());

        // start date is later
        startDate = dateFormat.format(new Date(actualEnd.getTime() + 1000L * 60 * 60 * 24 * 2));
        endDate = dateFormat.format(new Date(actualEnd.getTime() + 1000L * 60 * 60 * 24 * 4));
        reportList = service.getReports(username, endDate, startDate);
        assertNotNull(reportList);
        assertTrue(reportList.isEmpty());

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
