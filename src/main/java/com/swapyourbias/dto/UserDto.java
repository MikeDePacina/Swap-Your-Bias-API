package com.swapyourbias.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {


    @Size(min = 2, max = 200, message = "username must be between 2-200 characters long")
    private String username;


    @Email
    private String email;

    @NotBlank
    private String password;
}
