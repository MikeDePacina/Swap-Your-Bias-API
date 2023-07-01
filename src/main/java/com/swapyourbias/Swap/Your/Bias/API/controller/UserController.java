package com.swapyourbias.Swap.Your.Bias.API.controller;

import com.swapyourbias.Swap.Your.Bias.API.dto.UserDto;
import com.swapyourbias.Swap.Your.Bias.API.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public UserDto login(@Valid @RequestBody UserDto userDto){
        return authService.signup(userDto);

    }

    @PostMapping("/signin")
    public String signUp(@Valid @RequestBody UserDto userDto){
        return authService.signin(userDto);
    }
}
