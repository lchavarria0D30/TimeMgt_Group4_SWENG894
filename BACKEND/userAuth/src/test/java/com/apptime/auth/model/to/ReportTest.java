package com.apptime.auth.model.to;

import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskReport;
import com.apptime.auth.model.TaskReportType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Qi Zhang
 * This is the unit test class for Report
 * Use Case: TMGP4-26, TMGP4-31
 */
@SpringBootTest
public class ReportTest {

    @Test
    public void testParse() {
        String username = UUID.randomUUID().toString();
        TaskReport taskReport = new TaskReport();
        Random random = new Random();
        taskReport.setId(random.nextInt());
        taskReport.setTaskId(random.nextLong());
        taskReport.setOwner(username);
        taskReport.setType(TaskReportType.EARLIER);
        taskReport.setEfficiency(random.nextInt(100));
        taskReport.setDifference(Duration.ofMinutes(30)); // 30 Minutes
        taskReport.setScheduledDuration(Duration.ofMinutes(75)); // 1 Hour 15 Minutes
        taskReport.setActualDuration(Duration.ofMinutes(60 * 24 * 2 + 300 + 1)); // 2 Days 5 Hours 1 Minute

        Task task = new Task();
        task.setUserName(username);
        task.setName("taskName");

        Report report = Report.parse(taskReport, task);
        assertEquals(taskReport.getId(), report.getId());
        assertEquals(taskReport.getTaskId(), report.getTaskId());
        assertEquals(task.getName(), report.getTaskName());
        assertEquals(taskReport.getOwner(), report.getOwner());;
        assertEquals(taskReport.getType(), report.getType());
        assertEquals(taskReport.getEfficiency(), report.getEfficiency());
        assertEquals("30 Minutes", report.getDifference());
        assertEquals("1 Hour 15 Minutes", report.getScheduledDuration());
        assertEquals("2 Days 5 Hours 1 Minute", report.getActualDuration());
        assertNull(report.getActualStartDate());
        assertNull(report.getActualStartTime());
        assertNull(report.getActualEndDate());
        assertNull(report.getActualEndTime());

        taskReport.setActualDuration(Duration.ofMinutes(60 * 24 + 60 + 1)); // 1 Day 1 Hour 1 Minute
        assertEquals("1 Day 1 Hour 1 Minute", Report.parse(taskReport, task).getActualDuration());

        Date actualStartDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24);
        taskReport.setActualStartDate(actualStartDate);

        report = Report.parse(taskReport, task);
        assertNotNull(report.getActualStartDate());
        assertNotNull(report.getActualStartTime());
        assertNull(report.getActualEndDate());
        assertNull(report.getActualEndTime());

        Date actualEndDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60);
        taskReport.setActualEndDate(actualEndDate);
        report = Report.parse(taskReport, task);
        assertNotNull(report.getActualStartDate());
        assertNotNull(report.getActualStartTime());
        assertNotNull(report.getActualEndDate());
        assertNotNull(report.getActualEndTime());

        taskReport.setActualStartDate(null);
        report = Report.parse(taskReport, task);
        assertNull(report.getActualStartDate());
        assertNull(report.getActualStartTime());
        assertNotNull(report.getActualEndDate());
        assertNotNull(report.getActualEndTime());
    }
}
