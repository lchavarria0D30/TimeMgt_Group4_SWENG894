package com.apptime.auth.repository;

import com.apptime.auth.model.TaskReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Qi Zhang
 * This is the repository for TaskReport
 * Use Case: TMGP4-26, TMGP4-31
 */
public interface TaskReportRepository extends JpaRepository<TaskReport, Integer> {
    List<TaskReport> findByOwner(String owner);

    TaskReport findByTaskId(long taskId);

    void deleteByTaskId(long taskId);
}
