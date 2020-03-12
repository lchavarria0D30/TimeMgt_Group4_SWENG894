package com.apptime.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

/**
 * @author Qi Zhang
 * This is the entry for Notification APIs
 * Use Case: TMGP4-40, TMGP4-38
 */
public abstract class AbstractionAuthenticationController {

    protected String getUsername(Authentication authentication) {
        authentication = getAuthentication(authentication);
        return authentication == null ? null : authentication.getName();
    }

    protected Authentication getAuthentication(Authentication authentication) {
        return authentication != null ? authentication : SecurityContextHolder.getContext().getAuthentication();
    }

    protected boolean checkRule(Authentication authentication, String expectedRole) {
        authentication = getAuthentication(authentication);
        if (authentication == null || CollectionUtils.isEmpty(authentication.getAuthorities())) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equalsIgnoreCase(expectedRole)) {
                return true;
            }
        }
        return false;
    }
}
