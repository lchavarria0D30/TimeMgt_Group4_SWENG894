package com.apptime.auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.util.Objects;

/**
 * @author Qi Zhang
 * This is the PO for TaskReport
 * Use Case: TMGP4-26, TMGP4-31
 */
@Entity
public class TaskReport {
    @Id
    @GeneratedValue
    private int id;

    private long taskId;

    private String owner;

    private TaskReportType type;

    private Duration difference;

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

    public Duration getDifference() {
        return difference;
    }

    public void setDifference(Duration difference) {
        this.difference = difference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskReport report = (TaskReport) o;
        return getId() == report.getId() &&
                getTaskId() == report.getTaskId() &&
                getOwner() == report.getOwner() &&
                getType() == report.getType() &&
                Objects.equals(getDifference(), report.getDifference());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTaskId(), getOwner(), getType(), getDifference());
    }

    @Override
    public String toString() {
        return "TaskReport{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", owner=" + owner +
                ", type=" + type +
                ", difference=" + difference +
                '}';
    }
}
