package com.apptime.auth.model.to;

import com.apptime.auth.model.TaskReport;
import com.apptime.auth.model.TaskReportType;

import java.text.SimpleDateFormat;
import java.time.Duration;

public class Report {
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String TIME_PATTERN = "hh:mm a";

    private int id;

    private long taskId;

    private String owner;

    private TaskReportType type;

    private String difference;

    private String scheduledDuration;

    private String actualDuration;

    private int efficiency;

    private String actualStartDate;

    private String actualStartTime;

    private String actualEndDate;

    private String actualEndTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public TaskReportType getType() {
        return type;
    }

    public void setType(TaskReportType type) {
        this.type = type;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public String getScheduledDuration() {
        return scheduledDuration;
    }

    public void setScheduledDuration(String scheduledDuration) {
        this.scheduledDuration = scheduledDuration;
    }

    public String getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(String actualDuration) {
        this.actualDuration = actualDuration;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }

    public String getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(String actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public String getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public String getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(String actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", owner='" + owner + '\'' +
                ", type=" + type +
                ", difference='" + difference + '\'' +
                ", scheduledDuration='" + scheduledDuration + '\'' +
                ", actualDuration='" + actualDuration + '\'' +
                ", efficiency=" + efficiency +
                ", actualStartDate='" + actualStartDate + '\'' +
                ", actualStartTime='" + actualStartTime + '\'' +
                ", actualEndDate='" + actualEndDate + '\'' +
                ", actualEndTime='" + actualEndTime + '\'' +
                '}';
    }

    public static Report parse(TaskReport taskReport) {
        Report report = new Report();
        report.setId(taskReport.getId());
        report.setTaskId(taskReport.getTaskId());
        report.setOwner(taskReport.getOwner());
        report.setType(taskReport.getType());
        report.setDifference(parseDuration(taskReport.getDifference()));
        report.setScheduledDuration(parseDuration(taskReport.getScheduledDuration()));
        report.setActualDuration(parseDuration(taskReport.getActualDuration()));
        report.setEfficiency(taskReport.getEfficiency());

        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        final SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_PATTERN);
        if (taskReport.getActualStartDate() != null) {
            report.setActualStartDate(dateFormat.format(taskReport.getActualStartDate()));
            report.setActualStartTime(timeFormat.format(taskReport.getActualStartDate()));
        }
        if (taskReport.getActualEndDate() != null) {
            report.setActualEndDate(dateFormat.format(taskReport.getActualEndDate()));
            report.setActualEndTime(timeFormat.format(taskReport.getActualEndDate()));
        }

        return report;
    }

    private static String parseDuration(Duration duration) {
        if (duration == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (duration.toDays() > 0) {
            long days = duration.toDays();
            sb.append(days).append(" ");
            if (days > 1) {
                sb.append("Days");
            } else {
                sb.append("Day");
            }
            duration = duration.minusDays(days);
        }

        if (duration.toHours() > 0) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            long hours = duration.toHours();
            sb.append(hours).append(" ");
            if (hours > 1) {
                sb.append("Hours");
            } else {
                sb.append("Hour");
            }
            duration = duration.minusHours(hours);
        }

        if (duration.toMinutes() > 0) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            long minutes = duration.toMinutes();
            sb.append(minutes).append(" ");
            if (minutes > 1) {
                sb.append("Minutes");
            } else {
                sb.append("Minute");
            }
        }

        return sb.toString();
    }
}
