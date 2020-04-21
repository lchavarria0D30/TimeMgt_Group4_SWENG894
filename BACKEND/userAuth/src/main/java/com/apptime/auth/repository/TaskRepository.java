package com.apptime.auth.repository;

import java.util.*;

import com.apptime.auth.model.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;

import com.apptime.auth.model.Task;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Bashiir Mohamed
 * this class represent jpa Task repository
 */
public interface TaskRepository extends JpaRepository<Task, Long>{
	
	//find task by username
	 List<Task> findByUserName(String user);
	//view task details
	Task findByIdAndUserName(Long id, String userName);
	Task findByUserNameAndState(String userName, TaskState state);

	Task deleteById(long id);
	@Query(value = "select u from Task u where u.scheduledstart BETWEEN :start AND :end and u.userName = :name")
    Set<Task> getTasksStartedLaterThan( Date start, Date end, String name);
}
