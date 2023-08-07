package com.swapyourbias.controller;

import com.swapyourbias.dto.JWTAuthResponse;
import com.swapyourbias.dto.LoginDto;
import com.swapyourbias.dto.PhotoCardDto;
import com.swapyourbias.dto.PhotoCardList;
import com.swapyourbias.model.PhotoCard;
import com.swapyourbias.repository.PhotoCardRepository;
import com.swapyourbias.service.PhotoCardService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PhotoCardControllerTest {

    //to get which port the test is running  on
    @LocalServerPort
    private int port;

    @Autowired
    private PhotoCardService photoCardService;

    @Autowired
    private PhotoCardRepository photoCardRepository;

    private static RestTemplate restTemplate;

    private static String token = null;
    private String baseUrl = "http://localhost";

    @BeforeAll
    public static void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp(){
        String authUrl = "http://localhost:" + port + "/api/auth/";
        baseUrl = baseUrl.concat(":").concat(port +"").concat("/api/photocards");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String loginRequestBody = "{\"usernameOrEmail\":\"mickeymouse@gmail.com\",\"password\":\"disneyland\"}";
        String signUpRequestBody = "{\"username\": \"Mickey\",\"email\":\"mickeymouse@gmail.com\", \"password\":\"disneyland\" }";

        HttpEntity<String> loginReq = new HttpEntity<>(loginRequestBody, headers);
        HttpEntity<String> signUpReq = new HttpEntity<>(signUpRequestBody,headers);

        ResponseEntity<String> signUpRes = restTemplate.postForEntity(authUrl + "signup", signUpReq, String.class);
        ResponseEntity<JWTAuthResponse> loginRes = restTemplate.postForEntity(authUrl + "signin", loginReq, JWTAuthResponse.class);
        token = loginRes.getBody().getAccessToken();
    }


    @Test
    public void testPostPhotocard(){
        PhotoCardDto photoCardDto = new PhotoCardDto();
        photoCardDto.setArtist("Grimes");
        photoCardDto.setImgPath("see/you/on/a/dark/night");
        photoCardDto.setUserID(1);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Authorization","Bearer " + token);
        HttpEntity<PhotoCardDto> req = new HttpEntity<>(photoCardDto, header);

        ResponseEntity<PhotoCardDto> res = restTemplate.postForEntity(baseUrl, req, PhotoCardDto.class);
        assertEquals("Grimes",res.getBody().getArtist());
        assertEquals("see/you/on/a/dark/night", res.getBody().getImgPath());
        assertEquals(1,res.getBody().getUserID());

    }

    @Test
    @Sql(statements = "INSERT INTO users(email,password,username) VALUES ('test@gmail.com', 'tunay41dawdadwdawd', 'eheads')" +
             ";" +
            "INSERT INTO photocards(id,artist,kpop_group,img_path,user_id) VALUES (1, 'Gowon', 'Loona', 'one/and/only', 1)"
        ,executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllPhotocards(){
        ResponseEntity<PhotoCardList> pcs = restTemplate.getForEntity(baseUrl, PhotoCardList.class);
        assertEquals( 1, pcs.getBody().getTotalElements());
        assertEquals( "Gowon", pcs.getBody().getPhotocards().get(0).getArtist());
        assertTrue(pcs.getBody().isLast());
    }

    @Test
    @Sql(statements = "INSERT INTO users(email,password,username) VALUES ('test@gmail.com', 'tunay41dawdadwdawd', 'eheads')" +
            ";" +
            "INSERT INTO photocards(id,artist,kpop_group,img_path,user_id) VALUES (1, 'Gowon', 'Loona', 'one/and/only', 1)"
            ,executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetPhotoById(){
        ResponseEntity<PhotoCardDto> pc = restTemplate.getForEntity(baseUrl + "/1", PhotoCardDto.class);
        assertEquals("Gowon", pc.getBody().getArtist());
        assertEquals("Loona", pc.getBody().getGroup());
        assertEquals("one/and/only", pc.getBody().getImgPath());
    }

    @Test
    public void testUpdatePhoto(){
        PhotoCardDto photoCardDto = new PhotoCardDto();
        PhotoCardDto updatedPhotoCardDto = new PhotoCardDto();

        photoCardDto.setArtist("Grimes");
        photoCardDto.setImgPath("see/you/on/a/dark/night");
        photoCardDto.setUserID(1);

        updatedPhotoCardDto.setArtist("Grimes");
        updatedPhotoCardDto.setImgPath("I/want/to/be/software");
        updatedPhotoCardDto.setUserID(1);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Authorization","Bearer " + token);
        HttpEntity<PhotoCardDto> req = new HttpEntity<>(photoCardDto, header);
        HttpEntity<PhotoCardDto> updatedReq = new HttpEntity<>(updatedPhotoCardDto, header);

        //check for posted pc
        ResponseEntity<PhotoCardDto> res = restTemplate.postForEntity(baseUrl, req, PhotoCardDto.class);
        assertEquals("Grimes",res.getBody().getArtist());
        assertEquals("see/you/on/a/dark/night", res.getBody().getImgPath());
        assertEquals(1,res.getBody().getUserID());

        //check if updated
//        restTemplate.put(baseUrl + "/" + res.getBody().getUserID() + "/" + res.getBody().getId(), updatedReq, res.getBody().getUserID(), res.getBody().getId());
        String updateUrl = baseUrl + "/" + res.getBody().getUserID() + "/" + res.getBody().getId();
        ResponseEntity<PhotoCardDto> updatedPc = restTemplate.exchange(updateUrl, HttpMethod.PUT, updatedReq, PhotoCardDto.class);
//        ResponseEntity<PhotoCardDto> updatedPc = restTemplate.getForEntity(baseUrl + "/" + res.getBody().getId(), PhotoCardDto.class);
        assertEquals("Grimes", updatedPc.getBody().getArtist());
        assertEquals("I/want/to/be/software", updatedPc.getBody().getImgPath());
        assertEquals(1,updatedPc.getBody().getUserID());
    }

    @Test
    public void testDeletePhoto(){
        PhotoCardDto photoCardDto = new PhotoCardDto();

        photoCardDto.setArtist("Grimes");
        photoCardDto.setImgPath("see/you/on/a/dark/night");
        photoCardDto.setUserID(1);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Authorization","Bearer " + token);
        HttpEntity<PhotoCardDto> req = new HttpEntity<>(photoCardDto, header);
        HttpEntity<Void> deleteReq = new HttpEntity<>(null, header);

        //check for posted pc
        ResponseEntity<PhotoCardDto> res = restTemplate.postForEntity(baseUrl, req, PhotoCardDto.class);
        assertEquals("Grimes",res.getBody().getArtist());
        assertEquals("see/you/on/a/dark/night", res.getBody().getImgPath());
        assertEquals(1,res.getBody().getUserID());

        //check if updated


        ResponseEntity<String> deletedPC = restTemplate.exchange(baseUrl + "/" + res.getBody().getId(), HttpMethod.DELETE, deleteReq, String.class);

       assertEquals(HttpStatus.OK,deletedPC.getStatusCode());
       assertEquals("Deleted",deletedPC.getBody());
    }
}
