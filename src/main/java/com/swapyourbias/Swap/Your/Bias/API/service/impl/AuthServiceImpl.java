package com.swapyourbias.Swap.Your.Bias.API.service.impl;

import com.swapyourbias.Swap.Your.Bias.API.dto.UserDto;
import com.swapyourbias.Swap.Your.Bias.API.model.User;
import com.swapyourbias.Swap.Your.Bias.API.repository.UserRepository;
import com.swapyourbias.Swap.Your.Bias.API.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    public AuthServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public String signin(UserDto userDto) {
        Optional<User> user = userRepository.findByUsernameOrEmail(userDto.getUsername(), userDto.getEmail());
        if(!user.isEmpty()){
            if(user.get().getPassword() == userDto.getPassword()) return "Succesfull log in";
            else return "Wrong password";
        }
        return "No such account";
    }

    @Override
    public UserDto signup(UserDto userDto) {
        User newUser = convertToModel(userDto);
        User savedUser = userRepository.save(newUser);
        return convertToDTO(savedUser);
    }

    private User convertToModel(UserDto userDto){
        return modelMapper.map(userDto,User.class);
    }

    private UserDto convertToDTO(User user){
        return modelMapper.map(user,UserDto.class);
    }
}
