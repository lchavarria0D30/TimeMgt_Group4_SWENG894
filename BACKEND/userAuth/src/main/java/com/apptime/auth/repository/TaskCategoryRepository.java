package com.apptime.auth.repository;

import com.apptime.auth.model.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Qi Zhang
 * The DAO to access data of Category
 * Use Cases: TMGP4-43, TMGP4-39
 */
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Integer> {
    @Query("SELECT r FROM TaskCategory r WHERE r.owner = ?1 OR r.isPublic IS TRUE ORDER BY r.name")
    List<TaskCategory> findAllAccessibleCategories(String owner);

    List<TaskCategory> findByOwner(String owner);
    List<TaskCategory> findByIsPublic(boolean isPublic);
    List<TaskCategory> findByName(String name);
}
