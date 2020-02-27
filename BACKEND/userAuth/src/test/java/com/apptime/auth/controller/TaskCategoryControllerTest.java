package com.apptime.auth.controller;

import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.to.Category;
import com.apptime.auth.repository.TaskCategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.apptime.auth.controller.JsonUtil.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Qi Zhang
 * The unit class for TaskCategoryController
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskCategoryControllerTest {
    private static final String USERNAME = "username";
    private static final String ADMIN_USERNAME = "admin";

    private MockMvc mockMvc;

    @Autowired
    private TaskCategoryRepository categoryRepository;

    @Autowired
    private WebApplicationContext context;

    private SecurityContext securityContext;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        categoryRepository.deleteAll();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    private void mockAuthentication() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(USERNAME);
        mockAuthentication(authentication);
    }

    private void mockAuthentication(Authentication authentication) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreatePrivateCategory() throws Exception {
        mockAuthentication();

        Category request = new Category();
        String name = UUID.randomUUID().toString();
        request.setName(name);
        mockMvc.perform(MockMvcRequestBuilders.post("/category")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

        List<TaskCategory> categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
        assertEquals(name, categories.iterator().next().getName());
        assertFalse(categories.iterator().next().isPublic());

        // create a category with same name
        mockMvc.perform(MockMvcRequestBuilders.post("/category")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
        categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());

        // create another category with different name
        String name2 = UUID.randomUUID().toString();
        Category request2 = new Category();
        request2.setName(name2);
        mockMvc.perform(MockMvcRequestBuilders.post("/category")
                .content(asJsonString(request2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

        categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty());
        assertEquals(2, categories.size());

        // get categories for current user
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/category/mine")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = mapper.readValue(content, List.class);
        assertEquals(2, list.size());
        for (Map<String, Object> map : list) {
            String cname = (String) map.get("name");
            boolean isPublic = (Boolean) map.get("public");
            assertTrue(cname.equals(name) || cname.equals(name2));
            assertFalse(isPublic);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreatePublicCategory() throws Exception {
        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                GrantedAuthority authority = (GrantedAuthority) () -> "ADMIN";
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(authority);
                return authorities;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return this;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return ADMIN_USERNAME;
            }
        };
        mockAuthentication(authentication);

        Category request = new Category();
        String name = UUID.randomUUID().toString();
        request.setName(name);
        mockMvc.perform(MockMvcRequestBuilders.post("/category/public")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

        List<TaskCategory> categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
        assertEquals(name, categories.iterator().next().getName());
        assertTrue(categories.iterator().next().isPublic());

        // create a category with same name
        mockMvc.perform(MockMvcRequestBuilders.post("/category/public")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
        categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());

        // create another category with different name
        String name2 = UUID.randomUUID().toString();
        Category request2 = new Category();
        request2.setName(name2);
        mockMvc.perform(MockMvcRequestBuilders.post("/category/public")
                .content(asJsonString(request2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

        categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty());
        assertEquals(2, categories.size());

        // get public categories
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/category/public")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        MvcResult result = actions.andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = mapper.readValue(content, List.class);
        assertEquals(2, list.size());
        for (Map<String, Object> map : list) {
            String cname = (String) map.get("name");
            boolean isPublic = (Boolean) map.get("public");
            assertTrue(cname.equals(name) || cname.equals(name2));
            assertTrue(isPublic);
        }

        // get public categories with non-Admin user
        Authentication nonAdminAuthentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                GrantedAuthority authority = (GrantedAuthority) () -> "USER";
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(authority);
                return authorities;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return this;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return USERNAME;
            }
        };
        mockAuthentication(nonAdminAuthentication);

        actions = mockMvc.perform(MockMvcRequestBuilders.get("/category/public")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        result = actions.andReturn();
        content = result.getResponse().getContentAsString();
        list = mapper.readValue(content, List.class);
        assertEquals(2, list.size());
        for (Map<String, Object> map : list) {
            String cname = (String) map.get("name");
            boolean isPublic = (Boolean) map.get("public");
            assertTrue(cname.equals(name) || cname.equals(name2));
            assertTrue(isPublic);
        }

        // try to create public categories with nonAdmin user
        request = new Category();
        request.setName(UUID.randomUUID().toString());
        mockMvc.perform(MockMvcRequestBuilders.post("/category/public")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }
}
