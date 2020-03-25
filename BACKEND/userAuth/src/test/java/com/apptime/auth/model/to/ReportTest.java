package com.apptime.auth.model.to;

import com.apptime.auth.model.TaskReport;
import com.apptime.auth.model.TaskReportType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Qi Zhang
 * This is the unit test class for Report
 * Use Case: TMGP4-26, TMGP4-31
 */
@SpringBootTest
public class ReportTest {

    @Test
    public void testParse() {
        TaskReport taskReport = new TaskReport();
        Random random = new Random();
        taskReport.setId(random.nextInt());
        taskReport.setTaskId(random.nextLong());
        taskReport.setOwner(UUID.randomUUID().toString());
        taskReport.setType(TaskReportType.EARLIER);
        taskReport.setEfficiency(random.nextInt(100));
        taskReport.setDifference(Duration.ofMinutes(30)); // 30 Minutes
        taskReport.setScheduledDuration(Duration.ofMinutes(75)); // 1 Hour 15 Minutes
        taskReport.setActualDuration(Duration.ofMinutes(60 * 24 * 2 + 300 + 1)); // 2 Days 5 Hours 1 Minute

        Report report = Report.parse(taskReport);
        assertEquals(taskReport.getId(), report.getId());
        assertEquals(taskReport.getTaskId(), report.getTaskId());
        assertEquals(taskReport.getOwner(), report.getOwner());;
        assertEquals(taskReport.getType(), report.getType());
        assertEquals(taskReport.getEfficiency(), report.getEfficiency());
        assertEquals("30 Minutes", report.getDifference());
        assertEquals("1 Hour 15 Minutes", report.getScheduledDuration());
        assertEquals("2 Days 5 Hours 1 Minute", report.getActualDuration());

        taskReport.setActualDuration(Duration.ofMinutes(60 * 24 + 60 + 1)); // 1 Day 1 Hour 1 Minute
        assertEquals("1 Day 1 Hour 1 Minute", Report.parse(taskReport).getActualDuration());
    }
}
