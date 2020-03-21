package com.apptime.auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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

    @OneToOne
    private Task task;

    private TaskReportType type;

    private Duration difference;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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
        TaskReport that = (TaskReport) o;
        return getId() == that.getId() &&
                Objects.equals(getTask(), that.getTask()) &&
                getType() == that.getType() &&
                Objects.equals(getDifference(), that.getDifference());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTask(), getType(), getDifference());
    }

    @Override
    public String toString() {
        return "TaskReport{" +
                "id=" + id +
                ", task=" + task +
                ", type=" + type +
                ", difference=" + difference +
                '}';
    }
}
