package com.apptime.auth.controller;

import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apptime.auth.model.ClientUser;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

  @GetMapping("/message")
  public ResponseEntity<ClientUser> message(Principal principal) {
    return ResponseEntity.ok(new ClientUser("username:"+ principal.getName(),"email:unknown" ));
  }
}
