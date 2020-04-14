package com.apptime.auth.repository;

import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.UserTaskSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Qi Zhang
 * The DAO to access data of UserTaskSummary
 * Use Case: TMGP4-319
 */
public interface UserTaskSummaryRepository extends JpaRepository<UserTaskSummary, Integer> {
    @Query("SELECT s FROM UserTaskSummary s WHERE s.username = ?1 AND s.categoryId = ?2")
    List<UserTaskSummary> findByUsernameCategory(String username, int categoryId);

    List<UserTaskSummary> findByUsername(String username);

}
