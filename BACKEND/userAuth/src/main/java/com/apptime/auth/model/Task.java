package com.apptime.auth.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

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
    private LocalDateTime duration;
    private String userName;
    private String Description;
    @Enumerated(EnumType.STRING)
    private TaskState state;

	public Task() { }

    public Date getScheduledstart() {
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


    public Task(String string, Date date, int i) {

    }

    public LocalDateTime getDuration() {
        return duration;
    }

    public void setDuration(LocalDateTime duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


}

