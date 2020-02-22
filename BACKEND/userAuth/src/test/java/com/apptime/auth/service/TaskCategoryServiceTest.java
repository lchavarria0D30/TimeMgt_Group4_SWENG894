package com.apptime.auth.service;

import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.repository.TaskCategoryRepository;
import com.apptime.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Qi Zhang
 * The unit test class for TaskCategoryService
 */
@SpringBootTest
public class TaskCategoryServiceTest {
    @Autowired
    private TaskCategoryService service;

    @Autowired
    public TaskCategoryRepository repository;

    @BeforeEach
    public void init() {
        repository.deleteAll();
    }

    @Test
    public void testCreateCategory() {
        final String username = UUID.randomUUID().toString();
        final String name = UUID.randomUUID().toString();
        TaskCategory createdCategory = service.createCategory(name, username, false);
        assertNotNull(createdCategory);

        List<TaskCategory> categories = repository.findByOwner(username);
        assertFalse(CollectionUtils.isEmpty(categories));
        assertEquals(1, categories.size());
        assertEquals(name, categories.iterator().next().getName());
        assertFalse(categories.iterator().next().isPublic());

        // create another category
        final String name2 = UUID.randomUUID().toString();
        createdCategory = service.createCategory(name2, username, true);
        assertNotNull(createdCategory);

        Map<String, Boolean> categoryMap = new HashMap<>();
        categoryMap.put(name, false);
        categoryMap.put(name2, true);

        categories = repository.findByOwner(username);
        assertFalse(CollectionUtils.isEmpty(categories));
        assertEquals(2, categories.size());

        for (TaskCategory category : categories) {
            assertTrue(categoryMap.containsKey(category.getName()));
            assertEquals(categoryMap.get(category.getName()), category.isPublic());
        }

        // create a private category with same name & same owner
        createdCategory = service.createCategory(name, username, false);
        assertNull(createdCategory);

        // create a private category with same name & different owner
        final String username2 = UUID.randomUUID().toString();
        createdCategory = service.createCategory(name, username2, false);
        assertNotNull(createdCategory);

        // create a public category with same name & same owner
        createdCategory = service.createCategory(name2, username, true);
        assertNull(createdCategory);

        // create a public category with same name & different owner
        createdCategory = service.createCategory(name2, username2, true);
        assertNull(createdCategory);
    }

    @Test
    public void testGetCategoriesByOwner() {
        final String username1 = UUID.randomUUID().toString();

        // create two categories for the owner1
        String owner1Name1 = UUID.randomUUID().toString();
        TaskCategory category1 = new TaskCategory(owner1Name1, username1, false);
        repository.save(category1);
        String owner1Name2 = UUID.randomUUID().toString();
        TaskCategory category2 = new TaskCategory(owner1Name2, username1, true);
        repository.save(category2);

        final String username2 = UUID.randomUUID().toString();
        // create one category for the owner2
        String owner2Name = UUID.randomUUID().toString();
        TaskCategory category3 = new TaskCategory(owner2Name, username2, true);
        repository.save(category3);

        // owner1 should have two categories
        Collection<TaskCategory> categories = service.getCategoriesByOwner(username1);
        assertFalse(CollectionUtils.isEmpty(categories));
        assertEquals(2, categories.size());

        // owner2 should have one category
        categories = service.getCategoriesByOwner(username2);
        assertFalse(CollectionUtils.isEmpty(categories));
        assertEquals(1, categories.size());
    }

    @Test
    public void testGetAllPublicCategories() {
        final String username1 = UUID.randomUUID().toString();
        // create two categories for the owner1
        String owner1Name1 = UUID.randomUUID().toString();
        TaskCategory category1 = new TaskCategory(owner1Name1, username1, false);
        repository.save(category1);
        String owner1Name2 = UUID.randomUUID().toString();
        TaskCategory category2 = new TaskCategory(owner1Name2, username1, true);
        repository.save(category2);

        Map<String, String> categoryToOwner = new HashMap<>();
        categoryToOwner.put(owner1Name2, username1);

        final String username2 = UUID.randomUUID().toString();
        // create one category for the owner2
        String owner2Name = UUID.randomUUID().toString();
        TaskCategory category3 = new TaskCategory(owner2Name, username2, true);
        repository.save(category3);

        categoryToOwner.put(owner2Name, username2);

        // there should have two public categories
        Collection<TaskCategory> categories = service.getAllPublicCategories();
        assertFalse(CollectionUtils.isEmpty(categories));
        assertEquals(2, categories.size());

        for (TaskCategory category : categories) {
            assertTrue(categoryToOwner.containsKey(category.getName()));
            assertEquals(category.getOwner(), categoryToOwner.get(category.getName()));
        }
    }
}
