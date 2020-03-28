package com.apptime.auth.repository;

import com.apptime.auth.model.TaskReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author Qi Zhang
 * This is the repository for TaskReport
 * Use Case: TMGP4-26, TMGP4-31, TMGP4-35
 */
public interface TaskReportRepository extends JpaRepository<TaskReport, Integer> {
    List<TaskReport> findByOwner(String owner);

    @Query(value = "SELECT r.* FROM task_report r LEFT JOIN task t ON r.task_id = t.id WHERE r.owner = :owner AND t.actual_end >= :startTime", nativeQuery = true)
    List<TaskReport> findByOwnerAfterDate(@Param("owner") String owner, @Param("startTime") Date startTime);

    @Query(value = "SELECT r.* FROM task_report r LEFT JOIN task t ON r.task_id = t.id WHERE r.owner = :owner AND t.actual_end < :endTime", nativeQuery = true)
    List<TaskReport> findByOwnerBeforeDate(@Param("owner") String owner, @Param("endTime") Date endTime);

    @Query(value = "SELECT r.* FROM task_report r LEFT JOIN task t ON r.task_id = t.id WHERE r.owner = :owner AND t.actual_end >= :startTime AND t.actual_end < :endTime", nativeQuery = true)
    List<TaskReport> findByOwnerInTimeRange(@Param("owner") String owner, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    TaskReport findByTaskId(long taskId);

    void deleteByTaskId(long taskId);
}
