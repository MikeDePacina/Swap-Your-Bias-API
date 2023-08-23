package com.swapyourbias.service;

import com.swapyourbias.dto.LoginDto;
import com.swapyourbias.dto.UserDto;
import com.swapyourbias.exception.APIException;
import com.swapyourbias.model.Role;
import com.swapyourbias.model.User;
import com.swapyourbias.repository.RoleRepository;
import com.swapyourbias.repository.UserRepository;
import com.swapyourbias.security.JwtTokenProvider;
import com.swapyourbias.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.ClassBasedNavigableIterableAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignin_Success() {
        // Mock authentication
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("some_generated_token");

        // Test the method
        String token = authService.signin(new LoginDto("testuser", "testpassword"));

        // Assertions
        assertEquals("some_generated_token", token);
        verify(authenticationManager).authenticate(any());
        verify(jwtTokenProvider).generateToken(authentication);
    }

    @Test
    public void testSignup_Success() {
        // Mock userDto
        UserDto userDto = new UserDto("testuser", "testemail@example.com", "testpassword");

        // Mock roleRepository
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);

        // Mock password encoding
        when(passwordEncoder.encode("testpassword")).thenReturn("encoded_testpassword");

        // Mock userRepository to return false for username and email exists
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("testemail@example.com")).thenReturn(false);

        // Test the method
        String result = authService.signup(userDto);

        // Assertions
        assertEquals("User registered successfully!", result);

        // Verify that userRepository.existsByUsername and userRepository.existsByEmail were called
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("testemail@example.com");

        // Verify that userRepository.save was called with the correct User object
        verify(userRepository).save(argThat(user -> {
            assertEquals("testuser",user.getUsername());
            assertEquals("testemail@example.com",user.getEmail());
            assertEquals("encoded_testpassword",user.getPassword());
            List<Role> roles = new ArrayList<>(user.getRoles());
            assertEquals("ROLE_USER",user.getRoles().iterator().next().getName());
            return true;
        }));
    }

    @Test
    public void testSignUp_UsernameAlreadyExists(){
        //Mock userDto
        UserDto userDto = new UserDto("existingUsername", "testemail@example.com", "testpassword");

        //Mock userRepository to return true for username exists
        when(userRepository.existsByUsername("existingUsername")).thenReturn(true);

        //Assert throws exception
        assertThrows(APIException.class, () -> authService.signup(userDto));

        // Verify that userRepository.existsByUsername was called
        verify(userRepository).existsByUsername("existingUsername");
        // Verify that userRepository.save was not called
        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    public void testSignUp_EmailAlreadyExists(){
        //Mock userDto
        UserDto userDto = new UserDto("testUsername", "existingEmail@example.com", "testpassword");

        //Mock userRepository to return true for email exists
        when(userRepository.existsByEmail("existingEmail@example.com")).thenReturn(true);

        //Assert throws exception
        assertThrows(APIException.class, () -> authService.signup(userDto));

        // Verify that userRepository.existsByEmail was called
        verify(userRepository).existsByEmail("existingEmail@example.com");
        // Verify that userRepository.save was not called
        verify(userRepository, never()).save(any(User.class));
    }


}