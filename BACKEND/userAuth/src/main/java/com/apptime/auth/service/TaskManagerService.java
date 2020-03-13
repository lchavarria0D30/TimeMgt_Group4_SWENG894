package com.apptime.auth.service;

import java.security.Principal;
import java.util.*;

import com.apptime.auth.config.TaskStateMachine;
import com.apptime.auth.model.TaskState;
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
import com.apptime.auth.model.TaskError;
/**
 * @author Bashiir Mohamed
 * this class represent  task business service layer.
 */
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
	public Task getTask(long id, String username) {
		return taskRepo.findByIdAndUserName(id,username);

	}
	public Task getTask(TaskState ts, String userName){
		return taskRepo.findByUserNameAndState(userName,ts);
	}
	//create task
	public Task createTask(Task task, String user) {
		// TODO Auto-generated method stub
		task.setUserName(user);
		TaskStateMachine.CREATE(task);
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
		task.setState(old.getState());
		taskRepo.save(task);
		notificationService.updateNotificationForTask(task);
		return task;
	}

	//delete task
	@Transactional
	public Task deleteTask(long id) {
		Task old = taskRepo.findById(id);
		if(!old.getState().equals(TaskState.CREATED) || !old.getState().equals(TaskState.COMPLETED))
			return null;
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


	/**
	 *
	 * @param taskId
	 * @param startDate
	 * @return
	 */
	public TaskState start(long  taskId, Date startDate , String userName){
		Task task = taskRepo.findById(taskId);
		TaskState ts = null;
		if(task != null){
			TaskStateMachine.START(task);
			task.setStart(startDate);
			taskRepo.save(task);
			ts = task.getState();
		}
		return ts;

	}

	/**
	 *
	 * @param taskId
	 * @return
	 */
	public TaskState pause(long  taskId){
		Task task = taskRepo.findById(taskId);
		TaskState ts = null;
		if(task != null){
			TaskStateMachine.PAUSE(task);
			taskRepo.save(task);
			ts = task.getState();
		}
		return ts;

	}

	public TaskState complete(long  taskId, Date endDate){
		Task task = taskRepo.findById(taskId);
		TaskState ts = null;
		if(task != null){
			TaskStateMachine.COMPLETE(task);
			task.setEnd(endDate);
			taskRepo.save(task);
			ts = task.getState();
		}
		return ts;

	}

}
