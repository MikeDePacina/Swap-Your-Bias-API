package com.swapyourbias.service;

import com.swapyourbias.dto.LoginDto;
import com.swapyourbias.dto.UserDto;

public interface AuthService {

    String signin(LoginDto loginDto);

    String signup(UserDto userDto);
}
