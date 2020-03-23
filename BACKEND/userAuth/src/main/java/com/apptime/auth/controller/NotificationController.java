package com.apptime.auth.controller;

import com.apptime.auth.model.Notification;
import com.apptime.auth.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author Qi Zhang
 * This is the entry for Notification APIs
 * Use Case: TMGP4-40, TMGP4-38
 */
@RestController
@RequestMapping(value = "/notification")
public class NotificationController extends AbstractionAuthenticationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "/new")
    public ResponseEntity<Collection<Notification>> retrieveUndeliveredNotifications(Authentication authentication) {
        String owner = getUsername(authentication);
        if (owner == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(notificationService.retrieveUndeliveredNotifications(owner), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/snooze")
    public ResponseEntity<Boolean> snoozeNotification(@PathVariable int id, Authentication authentication) {
        String owner = getUsername(authentication);
        if (owner == null) {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
        boolean b = notificationService.snoozeNotification(id, owner);
        if (!b) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteNotification(@PathVariable int id, Authentication authentication) {
        String owner = getUsername(authentication);
        if (owner == null) {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
        boolean b = notificationService.deleteNotification(id, owner);
        if (!b) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
    }
}
