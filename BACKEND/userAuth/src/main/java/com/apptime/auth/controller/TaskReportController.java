package com.apptime.auth.controller;

import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskReport;
import com.apptime.auth.model.TaskState;
import com.apptime.auth.model.to.Report;
import com.apptime.auth.service.TaskManagerService;
import com.apptime.auth.service.TaskReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Qi Zhang
 * This is the entry for TaskReport APIs
 * Use Case: TMGP4-26, TMGP4-31, TMGP4-35
 */
@RestController
@RequestMapping(value = "/report")
public class TaskReportController extends AbstractionAuthenticationController {

    @Autowired
    private TaskReportService service;

    @Autowired
    private TaskManagerService taskManagerService;

    @GetMapping
    public ResponseEntity<Collection<Report>> getReports(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, Authentication authentication) {
        String username = getUsername(authentication);
        if (username == null || username.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<TaskReport> reports = service.getReports(username, startDate, endDate);
        List<Long> taskIdList = reports.stream().map(TaskReport::getTaskId).collect(Collectors.toList());
        List<Task> tasks = taskManagerService.getTasks(taskIdList);
        Map<Long, Task> taskMap = new HashMap<>();
        for (Task task : tasks) {
            taskMap.put(task.getId(), task);
        }
        return new ResponseEntity<>(service.getReports(username, startDate, endDate).stream().map(report -> Report.parse(report, taskMap.get(report.getTaskId()))).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/task/{taskId}")
    public ResponseEntity<Report> getByTask(@PathVariable Long taskId, Authentication authentication) {
        String username = getUsername(authentication);
        if (username == null || username.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Task task = taskManagerService.getTask(taskId, username);
        if (task == null || task.getState() != TaskState.COMPLETED) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TaskReport taskReport = service.findByTaskId(taskId);
        if (taskReport == null || !username.equals(taskReport.getOwner())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Report.parse(taskReport, task), HttpStatus.OK);
    }
}
