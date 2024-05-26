package ru.shmvsky.banking_api.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shmvsky.banking_api.dto.TokenRequest;
import ru.shmvsky.banking_api.dto.TokenResponse;
import ru.shmvsky.banking_api.service.UserService;
import ru.shmvsky.banking_api.util.JwtUtils;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    public TokenController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public TokenResponse generateTokenFor(@RequestBody TokenRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        var user = userService.loadUserByUsername(request.username());
        var token = jwtUtils.generateToken(user);

        return new TokenResponse(token);
    }

}
