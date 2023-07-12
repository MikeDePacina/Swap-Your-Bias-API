package com.swapyourbias.controller;

import com.swapyourbias.dto.JWTAuthResponse;
import com.swapyourbias.dto.LoginDto;
import com.swapyourbias.dto.UserDto;
import com.swapyourbias.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/signup","/register"})
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto){
        String response = authService.signup(userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping(value = {"/signin","/login"})
    public ResponseEntity<JWTAuthResponse> login(@Valid @RequestBody LoginDto loginDto){
        String token = authService.signin(loginDto);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }
}
