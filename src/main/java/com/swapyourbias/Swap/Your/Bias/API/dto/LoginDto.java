package com.swapyourbias.Swap.Your.Bias.API.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto {

    @NotBlank
    private String usernameOrEmail;
    @NotBlank
    private String password;

}
