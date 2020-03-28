package com.apptime.auth.model;

import javax.persistence.*;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Bashiir Mohamed
 * This is the model that represent Task
 */
@Entity
public class Task {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private Date scheduledstart;
    @Column(nullable = true)
    private Duration duration;
    private String userName;
    private String Description;
    @Enumerated(EnumType.STRING)
    private TaskState state;
    private Date actualStart;
    private Date actualEnd;
    private Date scheduledEnd;

    @ManyToMany(fetch=FetchType.EAGER)
    private Set<TaskCategory> categories;

    public Date getEnd() {
        return actualEnd;
    }
    public void setEnd(Date end) {
        this.actualEnd = end;
    }
    public Date getScheduledstart()
    {

       return scheduledstart;
    }

    public void setScheduledstart(Date scheduledstart) {


       this.scheduledstart = scheduledstart;
    }

    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public TaskState getState() {
        return state;
    }
    public void setStart(Date start) {
        this.actualStart = start;
    }
    public Date getStart() {
        return actualStart;
    }
    public Date getScheduledEnd() {
        return scheduledEnd;
    }

    public void setScheduledEnd(Date scheduledEnd) {

        this.scheduledEnd = scheduledEnd;
    }

    public Set<TaskCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<TaskCategory> categories) {
        this.categories = categories;
    }

    public void addCategory(TaskCategory category) {
        if (categories == null) {
            categories = new HashSet<>();
        }

        boolean existing = false;
        for (TaskCategory tc : categories) {
            if (tc.getId() == category.getId() || (tc.getName() != null && tc.getName().equals(category.getName()))) {
                existing = true;
                break;
            }
        }
        if (!existing) {
            categories.add(category);
        }
    }
}

