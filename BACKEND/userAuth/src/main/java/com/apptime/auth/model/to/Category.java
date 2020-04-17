package com.apptime.auth.model.to;

import com.apptime.auth.model.TaskCategory;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Qi Zhang
 * This is a TO (Transient Object) for Category
 */
public class Category {
    private int id;
    private String name;
    private boolean isPublic;

    public Category() {

    }

    public Category(int id, String name, boolean isPublic) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static Category parse(TaskCategory po) {
        if (po == null) {
            return null;
        }
        return new Category(po.getId(), po.getName(), po.isPublic());
    }

    public static Collection<Category> parse(Collection<TaskCategory> pos) {
        return pos.stream().map(Category::parse).collect(Collectors.toSet());
    }
}
