package com.swapyourbias.Swap.Your.Bias.API.service;

import com.swapyourbias.dto.PhotoCardDto;
import com.swapyourbias.dto.PhotoCardList;
import com.swapyourbias.model.PhotoCard;
import com.swapyourbias.model.User;
import com.swapyourbias.repository.PhotoCardRepository;
import com.swapyourbias.repository.UserRepository;
import com.swapyourbias.service.impl.PhotoCardServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PhotoCardServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhotoCardRepository photoCardRepository;
    @Autowired
    private ModelMapper modelMapper;

    private PhotoCardServiceImpl photoCardService;

    private User savedUser;
    @BeforeEach
    public void setUp(){
        photoCardService = new PhotoCardServiceImpl( photoCardRepository,
                                                     userRepository,
                                                     modelMapper);
        User user = new User();
        user.setUsername("Grimes");
        user.setEmail("c@gmail.com");
        user.setPassword("genesis");
        savedUser = userRepository.save(user);

    }



    @Test
    @Transactional
    public void testPostPhoto(){
        PhotoCardDto photoCardDto = new PhotoCardDto();
        photoCardDto.setUserID(savedUser.getId());
        photoCardDto.setArtist("C");
        photoCardDto.setImgPath("seeyou/on/adark/night");

        PhotoCardDto result = photoCardService.postPhoto(photoCardDto);

        PhotoCard savedPhotoCard = photoCardRepository.findById(result.getId()).orElse(null);

        assertNotNull(savedPhotoCard);
        assertThat(savedPhotoCard.getArtist()).isEqualTo("C");
        assertThat(savedPhotoCard.getImgPath()).isEqualTo("seeyou/on/adark/night");
        assertThat(savedPhotoCard.getOwner().getUsername()).isEqualTo("Grimes");

    }

    @Test
    @Transactional
    public void testGetAllPhotoCards(){
        PhotoCardDto photoCardDto1 = new PhotoCardDto();
        PhotoCardDto photoCardDto2 = new PhotoCardDto();
        PhotoCardDto photoCardDto3 = new PhotoCardDto();

        photoCardDto1.setArtist("Gowon");
        photoCardDto1.setGroup("Loona");
        photoCardDto1.setImgPath("love/4eva");
        photoCardDto1.setUserID(savedUser.getId());
        photoCardDto2.setArtist("Olivia Hye");
        photoCardDto2.setGroup("Loona");
        photoCardDto2.setImgPath("love/yyxy");
        photoCardDto2.setUserID(savedUser.getId());
        photoCardDto3.setArtist("Heejin");
        photoCardDto3.setGroup("Loona");
        photoCardDto3.setImgPath("1/3");
        photoCardDto3.setUserID(savedUser.getId());


        PhotoCard photo1 = photoCardRepository.save(modelMapper.map(photoCardDto1,PhotoCard.class));
        photo1.setOwner(savedUser);
        PhotoCard photo2 = photoCardRepository.save(modelMapper.map(photoCardDto2,PhotoCard.class));
        photo2.setOwner(savedUser);
        PhotoCard photo3 = photoCardRepository.save(modelMapper.map(photoCardDto3,PhotoCard.class));
        photo3.setOwner(savedUser);


        PhotoCardList result = photoCardService.getAllPhotocards(0,3,"id","asc");

        assertNotNull(result);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getPageNo()).isEqualTo(0);
        assertTrue(result.isLast());
        assertThat(result.getPageSize()).isEqualTo(3);
        assertThat(result.getPhotocards().get(0).getArtist()).isEqualTo("Gowon");
        assertThat(result.getPhotocards().get(0).getGroup()).isEqualTo("Loona");
        assertThat(result.getPhotocards().get(1).getArtist()).isEqualTo("Olivia Hye");
        assertThat(result.getPhotocards().get(2).getArtist()).isEqualTo("Heejin");
        assertThat(result.getPhotocards().get(2).getUserID()).isEqualTo(savedUser.getId());

    }

    @Test
    @Transactional
    public void testGetPhotoCard(){
        PhotoCardDto photoCardDto = new PhotoCardDto();
        photoCardDto.setArtist("Grimes");
        photoCardDto.setImgPath("genesis/txt");
        photoCardDto.setUserID(savedUser.getId());
        PhotoCard photoCard = modelMapper.map(photoCardDto,PhotoCard.class);
        photoCard.setOwner(savedUser);
        PhotoCard savedPC = photoCardRepository.save(photoCard);
        Optional<PhotoCard> ans = photoCardRepository.findById(savedPC.getId());
        PhotoCardDto expectedDto = modelMapper.map(ans,PhotoCardDto.class);
        expectedDto.setUserID(savedPC.getOwner().getId());
        PhotoCardDto res = photoCardService.getPhotocard(savedPC.getId());

        assertNotNull(ans);
        assertThat(expectedDto.getArtist()).isEqualTo(res.getArtist());
        assertThat(expectedDto.getImgPath()).isEqualTo(res.getImgPath());
        assertThat(expectedDto.getId()).isEqualTo(res.getId());
        assertThat(expectedDto.getUserID()).isEqualTo(res.getUserID());

    }

    @Test
    @Transactional
    public void testUpdatePhotoCard(){
        PhotoCardDto photoCardDto = new PhotoCardDto();
        PhotoCardDto updateCard = new PhotoCardDto();
        photoCardDto.setArtist("Grimes");
        photoCardDto.setImgPath("genesis/txt");
        photoCardDto.setUserID(savedUser.getId());
        updateCard.setImgPath("oblivion/file");

        PhotoCard photoCard = modelMapper.map(photoCardDto,PhotoCard.class);
        PhotoCard savedPC = photoCardRepository.save(photoCard);
        savedPC.setOwner(savedUser);
        PhotoCardDto expectedDto = modelMapper.map(savedPC,PhotoCardDto.class);
        expectedDto.setUserID(savedPC.getOwner().getId());
        expectedDto.setImgPath("oblivion/file");

        long pcID = savedPC.getId();
        long userID =  savedPC.getOwner().getId();

        PhotoCardDto result = photoCardService.updatePhotocard(updateCard,pcID,userID);

        assertThat(result.getArtist()).isEqualTo(expectedDto.getArtist());
        assertThat(result.getImgPath()).isEqualTo(expectedDto.getImgPath());
        assertThat(result.getUserID()).isEqualTo(expectedDto.getUserID());


    }

    @Test
    @Transactional
    public void testDeletePhotoCard(){
        PhotoCardDto photoCardDto = new PhotoCardDto();
        PhotoCard photo;
        PhotoCard savedPhoto;

        photoCardDto.setArtist("Grimes");
        photoCardDto.setImgPath("test/file");
        photo = modelMapper.map(photoCardDto,PhotoCard.class);
        savedPhoto = photoCardRepository.save(photo);
        savedPhoto.setOwner(savedUser);

        photoCardService.deletePhotocard(savedPhoto.getId());

        PhotoCard photoCard = photoCardRepository.findById(savedPhoto.getId()).orElse(null);

        assertNull(photoCard);
    }

}
