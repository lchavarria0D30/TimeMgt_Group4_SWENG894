package com.apptime.auth.repository;

import com.apptime.auth.model.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Qi Zhang
 * The DAO to access data of Category
 * Use Cases: TMGP4-43, TMGP4-39
 */
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Integer> {
    List<TaskCategory> findByOwner(String owner);
    List<TaskCategory> findByIsPublic(boolean isPublic);
    List<TaskCategory> findByName(String name);
}
