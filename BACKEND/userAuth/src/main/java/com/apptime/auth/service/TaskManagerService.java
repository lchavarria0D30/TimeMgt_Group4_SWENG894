package com.apptime.auth.service;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.apptime.auth.config.TaskStateMachine;
import com.apptime.auth.model.Prediction;
import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.TaskState;
import com.apptime.auth.model.Prediction;
import com.apptime.auth.repository.TaskCategoryRepository;
import com.apptime.auth.repository.TaskReportRepository;
import com.apptime.auth.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

/**
 * @author Bashiir Mohamed
 * this class represent  task business service layer.
 */
@Service
public class TaskManagerService {
	@Autowired
	TaskRepository taskRepo;


	@Autowired
	private TaskReportRepository reportRepository;

	@Autowired
	private TaskReportService reportService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private TaskCategoryRepository categoryRepository;

	//view task details
	//view task details
	public Task getTask(long id) {
		Optional<Task> taskOptional = taskRepo.findById(id);
		return taskOptional.orElse(null);
	}

	public Task getTask(long id, String username) {
		Task temp = taskRepo.findByIdAndUserName(id,username);
		return temp;

	}

	public Task getTask(TaskState ts, String userName){
		return taskRepo.findByUserNameAndState(userName,ts);

	}

	public List<Task> getTasks(Collection<Long> ids) {
		return taskRepo.findAllById(ids);
	}

	//create task
	public Task createTask(Task task, String user) {
		task.setUserName(user);
		TaskStateMachine.CREATE(task);
		updateCategories(task);
		System.out.println("Before saving in Db in CreatTask: +"+task.getScheduledstart());
		taskRepo.save(task);
		Task task2 = getTask(task.getId());
		System.out.println("task after saving to the database CreatTask"+task2.getScheduledstart());
		notificationService.createNotificationForTask(task);
		return task;
	}

	private void updateCategories(Task task) {
		Set<TaskCategory> categories = task.getCategories();
		if (categories == null || categories.isEmpty()) {
			return;
		}

		Map<Integer, TaskCategory> idCategoryMap = new HashMap<>();
		Map<String, TaskCategory> nameCategoryMap = new HashMap<>();
		List<TaskCategory> allAccessibleCategories = categoryRepository.findAllAccessibleCategories(task.getUserName());
		for (TaskCategory cat : allAccessibleCategories) {
			idCategoryMap.put(cat.getId(), cat);
			nameCategoryMap.put(cat.getName(), cat);
		}

		Set<TaskCategory> realCategories = new HashSet<>();
		for (TaskCategory category : categories) {
			TaskCategory categoryInDb;
			if (category.getId() > 0) {
				categoryInDb = idCategoryMap.get(category.getId());
			} else {
				categoryInDb = nameCategoryMap.get(category.getName());
			}
			if (categoryInDb != null) {
				realCategories.add(categoryInDb);
			}
		}
		task.setCategories(realCategories);
	}

	//update task
	public Task updateTask(@RequestBody Task task, String username) {
		Task old = getTask(task.getId());
		if (old == null || !old.getUserName().equals(username)) {
			return null;
		}
		task.setId(old.getId());
		task.setUserName(username);
		task.setState(old.getState());
		updateCategories(task);
		taskRepo.save(task);
		notificationService.updateNotificationForTask(task);
		return task;
	}

	//delete task
	@Transactional
	public Task deleteTask(long id) {
		Task old = getTask(id);
		if (old != null && old.getState() != null && !old.getState().equals(TaskState.CREATED) && !old.getState().equals(TaskState.COMPLETED)) {
			return null;
		}
		if (old != null) {
			reportRepository.deleteByTaskId(id);
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

	void setTaskRepo(TaskRepository taskRepo) {
		this.taskRepo = taskRepo;
	}

	void setReportService(TaskReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 *
	 * @param taskId
	 * @return
	 */
	@Transactional
	public TaskState start(long taskId){
		Task task = getTask(taskId);
		TaskState ts = null;
		if(task != null){
			TaskStateMachine.START(task);
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
	@Transactional
	public TaskState pause(long taskId){
		Task task = getTask(taskId);
		TaskState ts = null;
		if(task != null){
			TaskStateMachine.PAUSE(task);
			taskRepo.save(task);
			ts = task.getState();
		}
		return ts;
	}

	@Transactional
	public TaskState complete(long taskId){
		Task task = getTask(taskId);
		TaskState ts = null;
		if(task != null){
			TaskStateMachine.COMPLETE(task);
			taskRepo.save(task);
			reportService.generateReport(task);
			ts = task.getState();
		}
		return ts;
	}

	public Set<Task> getTasksStartedLaterThan(Date start, String name) {
		String pattern = "yyyy-MM-dd";
		Calendar c = Calendar.getInstance();
		c.setTime(start);
		c.add(Calendar.DATE, 1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		//Date sDate = new Date(start.getTime());
		Date eDate = c.getTime();
		Set<Task> result = new HashSet<Task>();
		result = taskRepo.getTasksStartedLaterThan(start,eDate,name);
		return result;
	}
	public Prediction getPrediction(int duration, int catergoryID)  {
		final String predictionEngineUrl = "http://localhost:5000/prediction/api/v1.0/task?plannedDuration=";
		final String prams = ""+duration+"&"+"Category="+catergoryID;
		Prediction result= null;
		RestTemplate restTemplate = new RestTemplate();
		result = restTemplate.getForObject(predictionEngineUrl+prams,Prediction.class);
		return result;
	}
}
