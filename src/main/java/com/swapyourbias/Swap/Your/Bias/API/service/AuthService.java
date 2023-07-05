package com.swapyourbias.Swap.Your.Bias.API.service;

import com.swapyourbias.Swap.Your.Bias.API.dto.LoginDto;
import com.swapyourbias.Swap.Your.Bias.API.dto.UserDto;

public interface AuthService {

    String signin(LoginDto loginDto);

    String signup(UserDto userDto);
}
