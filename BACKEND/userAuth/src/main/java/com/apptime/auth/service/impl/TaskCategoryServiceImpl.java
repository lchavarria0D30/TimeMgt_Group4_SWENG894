package com.apptime.auth.service.impl;

import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.Users;
import com.apptime.auth.repository.TaskCategoryRepository;
import com.apptime.auth.service.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author Qi Zhang
 * The implementation of TaskCategoryService
 */
@Service
public class TaskCategoryServiceImpl implements TaskCategoryService {
    @Autowired
    TaskCategoryRepository taskCategoryRepository;

    @Override
    public TaskCategory createCategory(String name, Users owner, boolean isPublic) {
        if (name == null || owner == null) {
            return null;
        }
//        Collection<TaskCategory> existingCategories = taskCategoryRepository.findByOwnerName(owner, name);
        Collection<TaskCategory> existingCategories = taskCategoryRepository.findByOwner(owner);
        if (existingCategories != null && !existingCategories.isEmpty() && !existingCategories.stream().filter(c -> name.equals(c.getName())).collect(Collectors.toSet()).isEmpty()) {
            return null;
        }

        if (isPublic) {
            existingCategories = taskCategoryRepository.findByIsPublic(true);
            if (existingCategories != null && !existingCategories.isEmpty() && !existingCategories.stream().filter(c -> name.equals(c.getName())).collect(Collectors.toSet()).isEmpty()) {
                return null;
            }
        }

        TaskCategory taskCategory = new TaskCategory(name, owner, isPublic);
        taskCategoryRepository.save(taskCategory);
        return taskCategory;
    }

    @Override
    public Collection<TaskCategory> getCategoriesByOwner(Users owner) {
        if (owner == null) {
            return Collections.emptyList();
        }
        return taskCategoryRepository.findByOwner(owner);
    }

    @Override
    public Collection<TaskCategory> getAllPublicCategories() {
        return taskCategoryRepository.findByIsPublic(true);
    }
}
