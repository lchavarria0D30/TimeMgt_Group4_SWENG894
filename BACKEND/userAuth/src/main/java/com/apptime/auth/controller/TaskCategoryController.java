package com.apptime.auth.controller;

import com.apptime.auth.model.Roles;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.Users;
import com.apptime.auth.model.to.Category;
import com.apptime.auth.repository.UserRepository;
import com.apptime.auth.service.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Qi Zhang
 * This is the entry for category APIs
 */
@RestController
@RequestMapping(value = "/category")
public class TaskCategoryController {

    private static final String ADMIN_ROLE = "ADMIN";

    @Autowired
    private TaskCategoryService categoryService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Category> createPrivateCategories(@RequestBody Category category, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = principal.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
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
        TaskCategory createdCategory = categoryService.createCategory(name, user, false);
        if (createdCategory != null) {
            return new ResponseEntity<>(Category.parse(createdCategory), HttpStatus.CREATED);
        } else {
            // duplicate name
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/mine")
    public ResponseEntity<Collection<Category>> getMineCategories(Principal principal) {
        if (principal == null) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED);
        }
        String username = principal.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            // wrong username
            return buildErrorResponse(HttpStatus.UNAUTHORIZED);
        }

        Collection<TaskCategory> categories = categoryService.getCategoriesByOwner(user);
        return new ResponseEntity<>(Category.parse(categories), HttpStatus.OK);
    }

    @GetMapping(value = "/public")
    public ResponseEntity<Collection<Category>> getPublicCategories(Principal principal) {
        if (principal == null) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED);
        }
        String username = principal.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            // wrong username
            return buildErrorResponse(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(Category.parse(categoryService.getAllPublicCategories()), HttpStatus.OK);
    }

    @PostMapping(value = "/public")
    public ResponseEntity<Category> createPublicCategory(@RequestBody Category category, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username = principal.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            // wrong username
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (category == null || category.getName() == null || category.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        boolean isAdmin = user.getRoles().stream().map(Roles::getRole).collect(Collectors.toSet()).contains(ADMIN_ROLE);
        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        TaskCategory createdCategory = categoryService.createCategory(category.getName(), user, true);
        return new ResponseEntity<>(Category.parse(createdCategory), HttpStatus.CREATED);
    }

    private ResponseEntity<Collection<Category>> buildErrorResponse(HttpStatus httpStatus) {
        return new ResponseEntity<>(httpStatus);
    }
}
