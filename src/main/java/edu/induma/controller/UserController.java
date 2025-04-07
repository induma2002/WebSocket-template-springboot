package edu.induma.controller;

import edu.induma.model.User;
import edu.induma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    final UserService userService;

    @GetMapping("/all")
    public List<User> getAllUsers() {
        try {
            return userService.get();
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/add")
    public String addUser(@RequestBody User user){
        try{
            userService.add(user);
            return "User added";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
