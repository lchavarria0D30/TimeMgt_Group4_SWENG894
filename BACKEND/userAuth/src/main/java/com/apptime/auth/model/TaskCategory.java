package com.apptime.auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Qi Zhang
 * Persistent Object for Category (and defining the data model)
 */
@Entity
public class TaskCategory {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private Users owner;
    private String name;
    private boolean isPublic;

    public TaskCategory() {
    }

    public TaskCategory(String name, Users owner, boolean isPublic) {
        this.owner = owner;
        this.name = name;
        this.isPublic = isPublic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Users getOwner() {
        return owner;
    }

    public void setOwner(Users owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
