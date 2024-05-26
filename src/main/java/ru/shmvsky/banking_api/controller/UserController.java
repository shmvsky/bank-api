package ru.shmvsky.banking_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.shmvsky.banking_api.dto.*;
import ru.shmvsky.banking_api.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/contacts")
    public UserResponse updateContacts(@RequestBody ContactsUpdateRequest contacts, Principal principal) {
        return userService.updateContacts(contacts, principal.getName());
    }

    @GetMapping("/search")
    public Page<UserResponse> search(@RequestParam Integer page, @RequestBody SearchRequest searchRequest) {
        return userService.search(page, searchRequest);
    }

    @PostMapping("/transfer")
    public TransferResponse search(@RequestBody TransferRequest transferRequest, Principal principal) {
        return userService.transfer(transferRequest, principal.getName());
    }

}
