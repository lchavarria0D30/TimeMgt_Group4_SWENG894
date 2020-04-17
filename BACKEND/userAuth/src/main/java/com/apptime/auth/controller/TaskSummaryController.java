package com.apptime.auth.controller;

import com.apptime.auth.model.AllUserTaskSummary;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.UserTaskSummary;
import com.apptime.auth.model.to.Category;
import com.apptime.auth.model.to.Summaries;
import com.apptime.auth.model.to.Summary;
import com.apptime.auth.service.TaskCategoryService;
import com.apptime.auth.service.TaskSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Qi Zhang
 * This is the entry for TaskSummary APIs
 * TMGP4-319: Persistent Object for Average Task Duration by Category for this User (and defining the data model)
 */
@RestController
@RequestMapping(value = "/summary")
public class TaskSummaryController extends AbstractionAuthenticationController {

    @Autowired
    private TaskSummaryService taskSummaryService;

    @Autowired
    private TaskCategoryService categoryService;

    @GetMapping("/mine")
    public ResponseEntity<Collection<Summaries>> getUserSummaries(Authentication authentication) {
        String username = getUsername(authentication);
        if (username == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<UserTaskSummary> userTaskSummaries = taskSummaryService.getUserTaskSummaries(username);
        if (userTaskSummaries == null || userTaskSummaries.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        List<Integer> categoryIds = userTaskSummaries.stream().map(UserTaskSummary::getCategoryId).collect(Collectors.toList());
        Collection<TaskCategory> categories = categoryService.getCategories(categoryIds);

        Map<Integer, TaskCategory> categoryMap = new HashMap<>();
        List<TaskCategory> publicCategories = new ArrayList<>();
        for (TaskCategory category : categories) {
            categoryMap.put(category.getId(), category);
            if (category.isPublic()) {
                publicCategories.add(category);
            }
        }

        List<AllUserTaskSummary> allUserTaskSummaries = taskSummaryService.getAllUserTaskSummariesByCategories(publicCategories);
        Map<Integer, AllUserTaskSummary> allUserTaskSummaryMap = new HashMap<>();
        for (AllUserTaskSummary allUserTaskSummary : allUserTaskSummaries) {
            allUserTaskSummaryMap.put(allUserTaskSummary.getCategoryId(), allUserTaskSummary);
        }

        List<Summaries> summariesList = new ArrayList<>();
        for (UserTaskSummary userTaskSummary : userTaskSummaries) {
            TaskCategory category = categoryMap.get(userTaskSummary.getCategoryId());
            AllUserTaskSummary allUserTaskSummary = allUserTaskSummaryMap.get(userTaskSummary.getCategoryId());
            Summaries summaries = new Summaries(Category.parse(category), Summary.parse(userTaskSummary, category), Summary.parse(allUserTaskSummary, category));
            summariesList.add(summaries);
        }

        return new ResponseEntity<>(summariesList, HttpStatus.OK);
    }

    @GetMapping(value = "/category/{categoryId}")
    public ResponseEntity<Summaries> getSummariesByCategory(@PathVariable int categoryId, Authentication authentication) {
        String username = getUsername(authentication);
        if (username == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<TaskCategory> categoryOptional = categoryService.getCategory(categoryId);
        if (!categoryOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TaskCategory category = categoryOptional.get();
        Optional<UserTaskSummary> userTaskSummaryOptional = taskSummaryService.getUserTaskSummaryByCategory(username, category);
        Optional<AllUserTaskSummary> allUserTaskSummaryOptional = category.isPublic() ? taskSummaryService.getAllUserTaskSummaryByCategory(category) : Optional.empty();

        Summaries summaries = new Summaries(Category.parse(category), Summary.parse(userTaskSummaryOptional.orElse(null), category), Summary.parse(allUserTaskSummaryOptional.orElse(null), category));
        return new ResponseEntity<>(summaries, HttpStatus.OK);
    }
}
