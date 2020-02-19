package com.apptime.auth.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apptime.auth.model.ClientUser;
import com.apptime.auth.model.Task;
import com.apptime.auth.model.Users;
import com.apptime.auth.service.TaskManagerService;

@RestController
@RequestMapping(value = "/tasks")
public class TaskManager {
	@Autowired
	TaskManagerService taskService;
	
	//list all task for a user
	@GetMapping(value = "/")
	public ResponseEntity<List<Task>> getTasks(Principal p) throws ParseException{
		String user = p.getName();
		List<Task> tasks = taskService.findUserTasks(user);
        return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
	}
	
	
	//view task details
	@GetMapping(value = "/task/{id}")
	public ResponseEntity<Task> getTask(@PathVariable("id") int taskId, Principal p) throws ParseException{
		Task task = taskService.getTask(taskId);
        return new ResponseEntity<Task>(task, HttpStatus.OK);
	}
	
	//create task
	@PostMapping("/newtask")
	public ResponseEntity<Object> createTask(@RequestBody Task task, Principal p) {
		String user = p.getName();
		if(taskService.getTask(task.getId())==null) {
		Task result = taskService.createTask(task,user);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
		} 
		return new ResponseEntity<Object>("{status: didn't create }", HttpStatus.NOT_FOUND);
	}
	//update task with put
	@PutMapping("/task")
	public ResponseEntity<Task> updateTask(@RequestBody Task task, Principal p) {
		
		return new ResponseEntity<Task>(taskService.updateTask(task, p.getName()), HttpStatus.OK);
	}
	@DeleteMapping("/Tasks/{id}")
	 Task deleteTask(@PathVariable Long id) {
	     return taskService.deleteTask(id);
	 }
	  
}