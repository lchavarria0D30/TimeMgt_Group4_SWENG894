package com.apptime.auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;

/**
 * @author Qi Zhang
 * TMGP4-319: Persistent Object for Average Task Duration by Category for this User (and defining the data model)
 */
@Entity
public class UserTaskSummary {
    @Id
    @GeneratedValue
    private int id;
    private String username;
    private int categoryId;
    private Duration averageDuration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Duration getAverageDuration() {
        return averageDuration;
    }

    public void setAverageDuration(Duration averageDuration) {
        this.averageDuration = averageDuration;
    }
}
