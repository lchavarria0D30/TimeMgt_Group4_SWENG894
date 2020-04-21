package com.apptime.auth.service;

import com.apptime.auth.model.TaskCategory;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Qi Zhang
 * The service interface for Category
 * Use Cases: TMGP4-43, TMGP4-39
 */
public interface TaskCategoryService {
    TaskCategory createCategory(String name, String owner, boolean isPublic);
    Collection<TaskCategory> getCategoriesByOwner(String owner);
    Collection<TaskCategory> getAllPublicCategories();
    Collection<TaskCategory> getAllAccessibleCategories(String owner);
    Optional<TaskCategory> getCategory(int id);
    Collection<TaskCategory> getCategories(Collection<Integer> ids);
}
