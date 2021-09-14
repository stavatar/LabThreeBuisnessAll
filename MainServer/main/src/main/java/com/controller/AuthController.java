package com.controller;


import com.Security.jwt.JwtProvider;
import com.entity.Users;

import com.controller.util.AuthRequest;
import com.controller.util.AuthResponse;
import com.controller.util.RegistrationRequest;

import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;

@Tag(name = "AuthController", description = "Содержит методы регистрации и авторизации")

@RestController
@Log
public class AuthController
{
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @Transactional
    @PostMapping("/register/")
    @Operation(summary = "Регистрация")
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        Users u = new Users();
        log.info("Пароль"+registrationRequest.getPassword());
        log.info("Логин"+registrationRequest.getLogin());
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        u.setLastdate(LocalDate.now());
        userService.createUser(u);
        return "OK";
    }

    @PostMapping("/auth/")
    @Operation(summary = "Авторизация")
    public AuthResponse auth(@RequestBody AuthRequest request ) throws ServletException
    {
        Users userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity.getLogin());
        log.info("Токен = "+token);
        userEntity.setLastdate(LocalDate.now());
        userService.save(userEntity);
        return new AuthResponse(token);
    }
}
