package com.apptime.auth.service.impl;

import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskReport;
import com.apptime.auth.model.TaskReportType;
import com.apptime.auth.repository.TaskReportRepository;
import com.apptime.auth.service.NotificationService;
import com.apptime.auth.service.TaskReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Qi Zhang
 * This is the Service implementation for TaskReport
 * Use Case: TMGP4-26, TMGP4-31, TMGP4-35
 */
@Service
public class TaskReportServiceImpl implements TaskReportService {
    private static final long TIME_GAP_IN_MIL_SEC = 1000L * 60 * 10; // ten minutes

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    @Autowired
    private TaskReportRepository reportRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public TaskReport generateReport(Task task) {
        if (task == null || task.getEnd() == null || task.getScheduledEnd() == null) {
            return null;
        }
        TaskReport existingReport = reportRepository.findByTaskId(task.getId());
        if (existingReport != null) {
            return null;
        }

        Date scheduledEnd = task.getScheduledEnd();
        Date actualEnd = task.getEnd();

        long gapInMilSec = actualEnd.getTime() - scheduledEnd.getTime();
        TaskReportType type;
        if (Math.abs(gapInMilSec) >= TIME_GAP_IN_MIL_SEC) {
            if (gapInMilSec > 0) {
                type = TaskReportType.LATER;
            } else {
                type = TaskReportType.EARLIER;
            }
        } else {
            type = TaskReportType.ON_TIME;
        }

        Duration gapDuration = Duration.ofMillis(Math.abs(gapInMilSec));

        TaskReport report = new TaskReport();
        report.setTaskId(task.getId());
        report.setOwner(task.getUserName());
        report.setType(type);
        report.setDifference(gapDuration);

        if (task.getDuration() != null && task.getScheduledstart() != null) {
            long scheduledDurationInMilSec = task.getScheduledEnd().getTime() - task.getScheduledstart().getTime();
            Duration scheduledDuration = Duration.ofMillis(scheduledDurationInMilSec);

            int efficiency = (int) ((task.getDuration().toMillis() * 100) / scheduledDurationInMilSec);

            report.setScheduledDuration(scheduledDuration);
            report.setActualDuration(task.getDuration());
            report.setEfficiency(efficiency);
        }

        reportRepository.save(report);

        notificationService.createNotificationForTaskReport(report);

        return report;
    }

    @Override
    public List<TaskReport> getReports(String owner) {
        return getReports(owner, null, null);
    }

    @Override
    public List<TaskReport> getReports(String owner, String startDate, String endDate) {
        if (owner == null || owner.isEmpty()) {
            return Collections.emptyList();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        Date startTimeDate = null;
        if (startDate != null && !startDate.isEmpty()) {
            try {
                startTimeDate = dateFormat.parse(startDate);
            } catch (Exception e) {
                e.printStackTrace();
                startTimeDate = null;
            }
        }

        Date endTimeDate = null;
        if (endDate != null && !endDate.isEmpty()) {
            try {
                Date date = dateFormat.parse(endDate);
                endTimeDate = new Date(date.getTime() + 1000L * 60 * 60 * 24); // the user inputted endtime would be included
            } catch (Exception e) {
                e.printStackTrace();
                endTimeDate = null;
            }
        }

        if (startTimeDate == null && endTimeDate == null) {
            return reportRepository.findByOwner(owner);
        }

        if (startTimeDate == null) {
            return reportRepository.findByOwnerBeforeDate(owner, endTimeDate);
        }

        if (endTimeDate == null) {
            return reportRepository.findByOwnerAfterDate(owner, startTimeDate);
        }

        return reportRepository.findByOwnerInTimeRange(owner, startTimeDate, endTimeDate);
    }

    @Override
    public TaskReport findByTaskId(long id) {
        return reportRepository.findByTaskId(id);
    }

    public void setReportRepository(TaskReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
}
