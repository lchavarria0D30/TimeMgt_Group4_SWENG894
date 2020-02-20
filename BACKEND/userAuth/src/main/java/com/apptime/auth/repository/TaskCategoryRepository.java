package com.apptime.auth.repository;

import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Qi Zhang
 * The DAO to access data of Category
 */
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Integer> {
    List<TaskCategory> findByOwner(Users owner);
    List<TaskCategory> findByIsPublic(boolean isPublic);
    //List<TaskCategory> findByOwnerName(Users owner, String name);
    // List<TaskCategory> findByNameIsPublic(String name, boolean isPublic);
}
