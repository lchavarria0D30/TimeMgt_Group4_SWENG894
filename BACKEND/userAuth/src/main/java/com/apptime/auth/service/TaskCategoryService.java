package com.apptime.auth.service;

import com.apptime.auth.model.TaskCategory;

import java.util.Collection;

/**
 * @author Qi Zhang
 * The service interface for Category
 * Use Cases: TMGP4-43, TMGP4-39
 */
public interface TaskCategoryService {
    TaskCategory createCategory(String name, String owner, boolean isPublic);
    Collection<TaskCategory> getCategoriesByOwner(String owner);
    Collection<TaskCategory> getAllPublicCategories();
}
