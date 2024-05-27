package ru.shmvsky.banking_api.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.shmvsky.banking_api.dto.RegisterRequest;
import ru.shmvsky.banking_api.model.User;
import ru.shmvsky.banking_api.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

    private final UserService userService;

    public ServiceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }


}
