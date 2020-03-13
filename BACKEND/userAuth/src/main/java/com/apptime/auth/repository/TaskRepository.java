package com.apptime.auth.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apptime.auth.model.Task;
/**
 * @author Bashiir Mohamed
 * this class represent jpa Task repository
 */
public interface TaskRepository extends JpaRepository<Task, Integer>{
	
	//find task by username
	 List<Task> findByUserName(String user);
//view task details
	Task findById(Long id);

	Task deleteById(long id);
	 
	 

}
