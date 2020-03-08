package com.apptime.auth.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.apptime.auth.model.Task;
import com.apptime.auth.repository.TaskRepository;

import javax.transaction.Transactional;

@Service
public class TaskManagerService {
	@Autowired
	TaskRepository taskRepo;

	@Autowired
	NotificationService notificationService;

    //view task details
	public Task getTask(long id) {
		return taskRepo.findById(id);

	}
//create task
	public Task createTask(Task task, String user) {
		// TODO Auto-generated method stub
		task.setUserName(user);
		taskRepo.save(task);
		notificationService.createNotificationForTask(task);
		return task;
	}


	//update task
	public Task updateTask(@RequestBody Task task, String username) {
		Task old = taskRepo.findById(task.getId());
		if (old == null || !old.getUserName().equals(username)) {
			return null;
		}
		task.setId(old.getId());
		task.setUserName(username);
		taskRepo.save(task);
		notificationService.updateNotificationForTask(task);
		return task;
	}

	//delete task
	@Transactional
	public Task deleteTask(long id) {
		Task old = taskRepo.findById(id);
		if (old != null) {
			taskRepo.delete(old);
			notificationService.deleteNotificationForTask(old);
		}
		return old;

	}
	public List<Task> findUserTasks(String user) {
		return taskRepo.findByUserName(user);
	}

	void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
}
