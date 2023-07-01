package com.swapyourbias.Swap.Your.Bias.API.service;

import com.swapyourbias.Swap.Your.Bias.API.dto.UserDto;

public interface AuthService {

    String signin(UserDto userDto);

    UserDto signup(UserDto userDto);
}
