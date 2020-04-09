package com.apptime.auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

/**
 * @author Qi Zhang
 * Persistent Object for Category (and defining the data model)
 */
@Entity
public class TaskCategory {
    @Id
    @GeneratedValue
    private int id;
    private String owner;
    private String name;
    private boolean isPublic;

    public TaskCategory() {
    }

    public TaskCategory(String name, String owner, boolean isPublic) {
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskCategory category = (TaskCategory) o;
        return getId() == category.getId() &&
                isPublic() == category.isPublic() &&
                Objects.equals(getOwner(), category.getOwner()) &&
                Objects.equals(getName(), category.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner(), getName(), isPublic());
    }

    @Override
    public String toString() {
        return "TaskCategory{" +
                "id=" + id +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", isPublic=" + isPublic +
                '}';
    }
}
