package com.apptime.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractControllerTest {
    protected static final String USERNAME = "username";

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    protected SecurityContext securityContext;

    protected void initMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    protected void mockAuthentication(String username) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        mockAuthentication(authentication);
    }

    protected void mockAuthentication() {
        mockAuthentication(USERNAME);
    }

    protected void mockAuthentication(Authentication authentication) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }
}
