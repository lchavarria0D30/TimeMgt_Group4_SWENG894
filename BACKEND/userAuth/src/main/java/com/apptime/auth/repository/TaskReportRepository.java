package com.apptime.auth.repository;

import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Qi Zhang
 * This is the repository for TaskReport
 * Use Case: TMGP4-26, TMGP4-31
 */
public interface TaskReportRepository extends JpaRepository<TaskReport, Integer> {
    @Query("SELECT r FROM TaskReport r WHERE r.task.userName = ?1")
    List<TaskReport> findByOwner(String owner);

    TaskReport findByTask(Task task);

    @Query("SELECT r FROM TaskReport r WHERE r.task.id = ?1")
    TaskReport findByTaskId(long taskId);

    void deleteByTask(Task task);
}
