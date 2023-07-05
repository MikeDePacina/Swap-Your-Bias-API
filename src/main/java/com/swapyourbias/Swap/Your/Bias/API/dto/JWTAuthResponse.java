package com.swapyourbias.Swap.Your.Bias.API.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JWTAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
}
