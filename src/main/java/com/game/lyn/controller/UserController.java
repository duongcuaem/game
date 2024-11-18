package com.game.lyn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.game.lyn.entity.User;
import com.game.lyn.service.UserService;

import java.util.Date;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<User> lockUserAccount(@PathVariable Long id) {
        User updatedUser = userService.lockUserAccount(id);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/unlock")
    public ResponseEntity<User> unlockUserAccount(@PathVariable Long id) {
        User updatedUser = userService.unlockUserAccount(id);
        return ResponseEntity.ok(updatedUser);
    }
}
