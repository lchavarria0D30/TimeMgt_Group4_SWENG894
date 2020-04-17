package com.apptime.auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;

/**
 * @author Qi Zhang
 * TMGP4-320: Persistent Object for Average Task Duration by Category for all Users (and defining the data model)
 */
@Entity
public class AllUserTaskSummary {
    @Id
    @GeneratedValue
    private int id;

    private int categoryId;

    private Duration averageDuration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
