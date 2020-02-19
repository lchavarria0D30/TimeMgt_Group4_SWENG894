package com.apptime.auth.service;

import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.Users;

import java.util.Collection;

/**
 * @author Qi Zhang
 * The service interface for Category
 */
public interface TaskCategoryService {
    TaskCategory createCategory(String name, Users owner, boolean isPublic);
    Collection<TaskCategory> getCategoriesByOwner(Users owner);
    Collection<TaskCategory> getAllPublicCategories();
}
