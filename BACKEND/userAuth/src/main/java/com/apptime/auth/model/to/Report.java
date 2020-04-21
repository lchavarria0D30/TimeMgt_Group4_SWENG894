package com.apptime.auth.model.to;

import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskReport;
import com.apptime.auth.model.TaskReportType;
import com.apptime.auth.util.DurationUtil;

import java.text.SimpleDateFormat;

public class Report {
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String TIME_PATTERN = "hh:mm a";

    private int id;

    private long taskId;

    private String taskName;

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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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
                ", taskName=" + taskName +
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

    public static Report parse(TaskReport taskReport, Task task) {
        if (taskReport == null || task == null || taskReport.getOwner() == null || !taskReport.getOwner().equals(task.getUserName())) {
            return null;
        }
        Report report = new Report();
        report.setId(taskReport.getId());
        report.setTaskId(taskReport.getTaskId());
        report.setTaskName(task.getName());
        report.setOwner(taskReport.getOwner());
        report.setType(taskReport.getType());
        report.setDifference(DurationUtil.toString(taskReport.getDifference()));
        report.setScheduledDuration(DurationUtil.toString(taskReport.getScheduledDuration()));
        report.setActualDuration(DurationUtil.toString(taskReport.getActualDuration()));
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
}
