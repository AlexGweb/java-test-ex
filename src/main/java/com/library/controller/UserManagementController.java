package com.library.controller;

import com.library.dto.ChangeRoleRequest;
import com.library.model.User;
import com.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserManagementController {
    private final UserService userService;

    @PutMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> changeUserRole(@RequestBody ChangeRoleRequest request) {
        User updatedUser = userService.changeUserRole(request);
        return ResponseEntity.ok(updatedUser);
    }
} 