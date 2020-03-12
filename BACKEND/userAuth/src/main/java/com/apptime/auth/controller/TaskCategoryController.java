package com.apptime.auth.controller;

import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.to.Category;
import com.apptime.auth.service.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author Qi Zhang
 * This is the entry for category APIs
 */
@RestController
@RequestMapping(value = "/category")
public class TaskCategoryController extends AbstractionAuthenticationController {

    private static final String ADMIN_ROLE = "ADMIN";

    @Autowired
    private TaskCategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createPrivateCategories(@RequestBody Category category, Authentication authentication) {
        String username = getUsername(authentication);
        if (username == null) {
            // wrong username
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (category == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String name = category.getName();
        if (name == null || name.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TaskCategory createdCategory = categoryService.createCategory(name, username, false);
        if (createdCategory != null) {
            return new ResponseEntity<>(Category.parse(createdCategory), HttpStatus.CREATED);
        } else {
            // duplicate name
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/mine")
    public ResponseEntity<Collection<Category>> getMineCategories(Authentication authentication) {
        String username = getUsername(authentication);
        if (username == null) {
            // wrong username
            return buildErrorResponse(HttpStatus.UNAUTHORIZED);
        }

        Collection<TaskCategory> categories = categoryService.getCategoriesByOwner(username);
        return new ResponseEntity<>(Category.parse(categories), HttpStatus.OK);
    }

    @GetMapping(value = "/public")
    public ResponseEntity<Collection<Category>> getPublicCategories(Authentication authentication) {
        String username = getUsername(authentication);
        if (username == null) {
            // wrong username
            return buildErrorResponse(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(Category.parse(categoryService.getAllPublicCategories()), HttpStatus.OK);
    }

    @PostMapping(value = "/public")
    public ResponseEntity<Category> createPublicCategory(@RequestBody Category category, Authentication authentication) {
        authentication = getAuthentication(authentication);
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username = authentication.getName();
        if (username == null) {
            // wrong username
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!checkRule(authentication, ADMIN_ROLE)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (category == null || category.getName() == null || category.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        TaskCategory createdCategory = categoryService.createCategory(category.getName(), username, true);
        if (createdCategory == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        return new ResponseEntity<>(Category.parse(createdCategory), HttpStatus.CREATED);
    }

    private ResponseEntity<Collection<Category>> buildErrorResponse(HttpStatus httpStatus) {
        return new ResponseEntity<>(httpStatus);
    }

}
