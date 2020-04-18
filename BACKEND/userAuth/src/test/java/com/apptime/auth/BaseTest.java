package com.apptime.auth;

import com.apptime.auth.model.Task;
import com.apptime.auth.repository.AllUserTaskSummaryRepository;
import com.apptime.auth.repository.NotificationRepository;
import com.apptime.auth.repository.TaskCategoryRepository;
import com.apptime.auth.repository.TaskReportRepository;
import com.apptime.auth.repository.TaskRepository;
import com.apptime.auth.repository.UserTaskSummaryRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseTest {
    @Autowired
    private AllUserTaskSummaryRepository allUserTaskSummaryRepository;
    @Autowired
    private UserTaskSummaryRepository userTaskSummaryRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private TaskCategoryRepository taskCategoryRepository;
    @Autowired
    private TaskReportRepository taskReportRepository;
    @Autowired
    private TaskRepository taskRepository;

    @AfterEach
    public void cleanup() {
        List<Task> taskList = taskRepository.findAll();
        for (Task task : taskList) {
            task.setCategories(null);
            taskRepository.save(task);
        }
        allUserTaskSummaryRepository.deleteAll();
        userTaskSummaryRepository.deleteAll();
        notificationRepository.deleteAll();
        taskCategoryRepository.deleteAll();
        taskReportRepository.deleteAll();
        taskRepository.deleteAll();
//       helper.cleanup();
    }

}
