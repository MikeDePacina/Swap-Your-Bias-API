package com.swapyourbias.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapyourbias.dto.JWTAuthResponse;
import com.swapyourbias.dto.LoginDto;

import com.swapyourbias.dto.UserDto;

import com.swapyourbias.repository.UserRepository;
import com.swapyourbias.service.AuthService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    //to get which port the test is running  on
    @LocalServerPort
    private int port;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;
    private static RestTemplate restTemplate;

    private String baseUrl = "http://localhost";

    @BeforeAll
    public static void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp(){
        baseUrl = baseUrl.concat(":").concat(port +"").concat("/api/auth");
    }

    @Test
    public void testSuccessfulSignUp(){
        UserDto userDto = new UserDto("newUsername01", "newUsername01@gmail.com", "newUserPassword");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> req = new HttpEntity<>(userDto, header);

        ResponseEntity<String> res = restTemplate.postForEntity(baseUrl + "/signup", req, String.class);

        assertEquals("User registered successfully!", res.getBody());
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
    }

    @Test
    public void testSuccessfulRegister(){
        UserDto userDto = new UserDto("newUser", "newUser@gmail.com", "newUserPassword");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> req = new HttpEntity<>(userDto, header);

        ResponseEntity<String> res = restTemplate.postForEntity(baseUrl + "/register", req, String.class);

        assertEquals("User registered successfully!", res.getBody());
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
    }

    @Test
    @ResponseStatus(HttpStatus.BAD_REQUEST) // This is the expected status if the username is less than 2 chars long
    public void testFailedSignUpDueToShortUsername() throws Exception{
        UserDto userDto = new UserDto("c", "uncontrollable@gmail", "newUserPassword");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> req = new HttpEntity<>(userDto, header);

        HttpClientErrorException err = assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/register", req, String.class);
        });

        String errMessage = err.getResponseBodyAsString();

        // Parse the JSON response using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(errMessage);

        // Access the message in the JSON response and assert its value
        String errorMessage = jsonNode.get("username").asText();
        assertEquals("username must be between 2-200 characters long", errorMessage);
    }

    @Test
    @Sql(statements = "INSERT INTO users(email,password,username) VALUES ('unique@email.com', 'tunay41dawdadwdawd', 'existingU')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void testSignUpWithExistingUsername() throws JsonProcessingException {
        UserDto userDto = new UserDto("existingU", "test@gmail.com","harana");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> req = new HttpEntity<>(userDto, header);

        HttpClientErrorException err = assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/register", req, String.class);
        });

        String errMessage = err.getResponseBodyAsString();

        // Parse the JSON response using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(errMessage);

        // Access the message in the JSON response and assert its value
        String errorMessage = jsonNode.get("message").asText();
        assertEquals("Username already exists!", errorMessage);

    }

    @Test
    @Sql(statements = "INSERT INTO users(email,password,username) VALUES ('existing@email.com', 'tunay41dawdadwdawd', 'Grimes')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void testSignUpWithExistingEmail() throws JsonProcessingException {
        UserDto userDto = new UserDto("uniqueName", "existing@email.com","harana");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> req = new HttpEntity<>(userDto, header);

        HttpClientErrorException err = assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/register", req, String.class);
        });

        String errMessage = err.getResponseBodyAsString();

        // Parse the JSON response using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(errMessage);

        // Access the message in the JSON response and assert its value
        String errorMessage = jsonNode.get("message").asText();
        assertEquals("Email already exists!", errorMessage);

    }

    @Test
    public void testSuccessfulSignIn(){
        UserDto userDto = new UserDto("testUsername01","testEmail01@gmail.com","Genesis");
        LoginDto loginDto = new LoginDto("testUsername01","Genesis");

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginDto> loginReq = new HttpEntity<>(loginDto, header);
        HttpEntity<UserDto>  userReq = new HttpEntity<>(userDto,header);

        //Create user first
        ResponseEntity<String> userRes = restTemplate.postForEntity(baseUrl + "/signup", userReq, String.class);
        ResponseEntity<JWTAuthResponse> loginRes = restTemplate.postForEntity(baseUrl + "/signin", loginReq, JWTAuthResponse.class);

        assertEquals(HttpStatus.CREATED, userRes.getStatusCode());
        assertEquals(HttpStatus.OK, loginRes.getStatusCode());

        //returns random access token
        assertEquals(String.class, loginRes.getBody().getTokenType().getClass());

    }

    @Test
    public void testSuccessfulLogIn(){
        UserDto userDto = new UserDto("testUsername02","testEmail02@gmail.com","Genesis");
        LoginDto loginDto = new LoginDto("testUsername02","Genesis");

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginDto> loginReq = new HttpEntity<>(loginDto, header);
        HttpEntity<UserDto>  userReq = new HttpEntity<>(userDto,header);

        //Create user first
        ResponseEntity<String> userRes = restTemplate.postForEntity(baseUrl + "/signup", userReq, String.class);
        ResponseEntity<JWTAuthResponse> loginRes = restTemplate.postForEntity(baseUrl + "/login", loginReq, JWTAuthResponse.class);

        assertEquals(HttpStatus.CREATED, userRes.getStatusCode());
        assertEquals(HttpStatus.OK, loginRes.getStatusCode());

        //returns random access token
        assertEquals(String.class, loginRes.getBody().getTokenType().getClass());

    }
    @Test
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void testWrongCredentialsSignIn(){
        LoginDto loginDto = new LoginDto("Grimes","Genesis");

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginDto> loginReq = new HttpEntity<>(loginDto, header);

        assertThrows(HttpServerErrorException.class, () -> {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/signin", loginReq, String.class);
        });

    }
}
