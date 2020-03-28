package com.apptime.auth.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.apptime.auth.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.apptime.auth.service.TaskManagerService;

/**
 * @author Bashiir Mohamed
 * This class is the controller for task managment api for each use.
 * it contains all funtionlity for managing taks.
 */
@RestController
@RequestMapping(value = "/tasks")
public class TaskManager {
	private static final String DATE_PATTERN = "yyyy-MM-dd";

	@Autowired
	TaskManagerService taskService;

	/**
	 *
	 * @param p the current authenticated user.
	 * @return List of tasks owned by the user.
	 * @throws ParseException json conversion Exception
	 */
	@GetMapping(value = "/")
	public ResponseEntity<List<Task>> getTasks(Principal p) {
		String user = getPrinciple(p).getName();
		List<Task> tasks = taskService.findUserTasks(user);
		return new ResponseEntity<List<Task>>(removeCategoryOwner(tasks), HttpStatus.OK);
	}

	private List<Task> removeCategoryOwner(List<Task> tasks) {
		if (tasks != null) {
			tasks.forEach(this::removeCategoryOwner);
		}
		return tasks;
	}

	private Set<Task> removeCategoryOwner(Set<Task> tasks) {
		if (tasks != null) {
			tasks.forEach(this::removeCategoryOwner);
		}
		return tasks;
	}

	private Task removeCategoryOwner(Task task) {
		if (task == null || task.getCategories() == null) {
			return task;
		}

		for (TaskCategory category : task.getCategories()) {
			category.setOwner(null);
		}
		return task;
	}

	/**
	 *
	 * @param taskId request task id.
	 * @param p current authenticated user(principle)
	 * @return single list with the given @id or @http.status.noFound.
	 * @throws ParseException json conversion Exception
	 */
	@GetMapping(value = "/task/{id}")
	public ResponseEntity<Task> getTask(@PathVariable("id") int taskId, Principal p) {
		Task task = taskService.getTask(taskId);
		if (task.getUserName().equals(getPrinciple(p).getName())) {
			return new ResponseEntity<Task>(removeCategoryOwner(task), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@PostMapping("/due/start")
	public ResponseEntity<Set<Task>> showAddedSince(@RequestBody FormatedDate start, Principal p) {
		Set<Task>  tasks = null;
		try {
			tasks = taskService.getTasksStartedLaterThan(parseDate(start.getDate()), getPrinciple(p).getName());
		} catch (ParseException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (tasks == null || tasks.isEmpty()) {
			return new ResponseEntity<>(Collections.emptySet(), HttpStatus.OK);

		} else {
			return new ResponseEntity<>(removeCategoryOwner(tasks), HttpStatus.OK);
		}
	}

	private Date parseDate(String str) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
		return dateFormat.parse(str);
	}

	/**
	 *
	 * @param task task data to be created
	 * @param p current authenticated user(principle)
	 * @return the currently created task
	 */
	@PostMapping("/newtask")
	public ResponseEntity<Object> createTask(@RequestBody Task task, Principal p) {
		//.out.println("frontend sent Date : "+ task.getScheduledstart().toString());
		String user = getPrinciple(p).getName();
		if(taskService.getTask(task.getId())==null) {
			Task result = taskService.createTask(task,user);
			return new ResponseEntity<Object>(removeCategoryOwner(result), HttpStatus.OK);
		}
		return new ResponseEntity<Object>("{status: didn't create }", HttpStatus.NOT_FOUND);
	}

	/**
	 *
	 * @param task the task to be updated.
	 * @param p current authenticated user(principle)
	 * @return returns the task that was updated
	 */
	@PutMapping("/task")
	public ResponseEntity<Task> updateTask(@RequestBody Task task, Principal p) {
		Task updatedTask = taskService.updateTask(task, getPrinciple(p).getName());
		if (updatedTask != null) {
			return new ResponseEntity<>(removeCategoryOwner(updatedTask), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 *
	 * @param id @id of the @task to be deleted.
	 * @param p current authenticated user(principle)
	 * @return returns the @ deleted task T where T.id = @id
	 */
	@DeleteMapping("/task/{id}")
	public ResponseEntity<Task> deleteTask(@PathVariable Long id, Principal p) {
		Task task = taskService.getTask(id);
		if (task == null || !getPrinciple(p).getName().equals(task.getUserName())) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(removeCategoryOwner(taskService.deleteTask(id)), HttpStatus.OK);
	}

	/**
	 *
	 * @param id
	 * @param p
	 * @return
	 */
	@PostMapping("/task/{id}/start")
	public ResponseEntity<?> start(@PathVariable Long id, Principal p){
		String userName = getPrinciple(p).getName();
		Task task = taskService.getTask(id,userName);
		if (task == null ) {
			return new ResponseEntity<TaskError>( new TaskError(ErrorType.Task_Not_Found, null), HttpStatus.NOT_FOUND);
		}
		//check for currently other active tasks
		Task active = taskService.getTask(TaskState.ACTIVE, userName);
		if(active != null){
			return new ResponseEntity<TaskError>(new TaskError(ErrorType.Concurrent_Active_Task_Not_Allowed, removeCategoryOwner(active)),HttpStatus.BAD_REQUEST);
		}
		if(!isAuthorized(p,task)){
			return new ResponseEntity<TaskError>(new TaskError(ErrorType.Task_Not_Found, null),HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<TaskState>(taskService.start(id), HttpStatus.OK);
	}

	/**
	 *
	 * @param id
	 * @param p
	 * @return
	 */
	@PostMapping("/task/{id}/pause")
	public ResponseEntity<?> pause(@PathVariable Long id, Principal p){
		String userName = getPrinciple(p).getName();
		Task task = taskService.getTask(id,userName);
		if (task == null ) {
			return new ResponseEntity<TaskError>( new TaskError(ErrorType.Task_Not_Found, null), HttpStatus.NOT_FOUND);
		}
		if(!isAuthorized(p,task)){
			return new ResponseEntity<TaskError>(new TaskError(ErrorType.Task_Not_Found, null),HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<TaskState>(taskService.pause(id), HttpStatus.OK);
	}

	/**
	 *
	 * @param id
	 * @param p
	 * @return
	 */
	@PostMapping("/task/{id}/complete")
	public ResponseEntity<?> complete(@PathVariable Long id, Principal p){
		Task task = taskService.getTask(id);
		if (task == null ) {
			return new ResponseEntity<TaskError>( new TaskError(ErrorType.Task_Not_Found, null), HttpStatus.NOT_FOUND);
		}
		if(!isAuthorized(p,task)){
			return new ResponseEntity<TaskError>(new TaskError(ErrorType.Task_Not_Found, null),HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<TaskState>(taskService.complete(id), HttpStatus.OK);
	}

	/**
	 *
	 * @param p current authenticated user(principle)
	 * @return rturns returns current authenticated user
	 * this method is private
	 */
	private Principal getPrinciple(Principal p) {
		return p != null ? p : SecurityContextHolder.getContext().getAuthentication();
	}

	private Boolean isAuthorized(Principal p, Task task){
		return task.getUserName().equals(getPrinciple(p).getName());
	}
}