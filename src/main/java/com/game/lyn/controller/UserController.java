 package com.game.lyn.controller;

 import com.game.lyn.entity.User;
 import com.game.lyn.service.UserService;
 import org.springframework.web.bind.annotation.*;

 @RestController
 @RequestMapping("/users")
 public class UserController {

     private final UserService userService;

     public UserController(UserService userService) {
         this.userService = userService;
     }

     @GetMapping("/{id}")
     public User getUser(@PathVariable Long id) {
         return userService.getUserById(id);
     }

     @PostMapping("/update")
     public User updateUser(@RequestBody User user) {
         return userService.updateUser(user);
     }

     @DeleteMapping("/{id}")
     public void deleteUser(@PathVariable Long id) {
         userService.deleteUser(id);
     }
 }
