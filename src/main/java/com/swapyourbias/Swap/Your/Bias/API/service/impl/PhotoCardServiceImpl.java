package com.swapyourbias.Swap.Your.Bias.API.service.impl;

import com.swapyourbias.Swap.Your.Bias.API.dto.PhotoCardDto;
import com.swapyourbias.Swap.Your.Bias.API.dto.PhotoCardList;
import com.swapyourbias.Swap.Your.Bias.API.exception.ResourceNotFoundException;
import com.swapyourbias.Swap.Your.Bias.API.model.PhotoCard;
import com.swapyourbias.Swap.Your.Bias.API.model.User;
import com.swapyourbias.Swap.Your.Bias.API.repository.PhotoCardRepository;
import com.swapyourbias.Swap.Your.Bias.API.repository.UserRepository;
import com.swapyourbias.Swap.Your.Bias.API.service.PhotoCardService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhotoCardServiceImpl implements PhotoCardService {

    private PhotoCardRepository photoCardRepository;

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    public PhotoCardServiceImpl(PhotoCardRepository photoCardRepository,
                                ModelMapper modelMapper,
                                UserRepository userRepository) {
        this.photoCardRepository = photoCardRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public PhotoCardDto postPhoto(PhotoCardDto photoCardDto) {
        PhotoCard photoCard = convertToModel(photoCardDto);
        PhotoCard savedPhotoCard = photoCardRepository.save(photoCard);
        return convertToDTO(savedPhotoCard);

    }

    @Override
    public PhotoCardList getAllPhotocards(int pageNo, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<PhotoCard> page = photoCardRepository.findAll(pageable);
        List<PhotoCard> photocards = page.getContent();
        List<PhotoCardDto> content = photocards.stream().map(photoCard -> convertToDTO(photoCard)).collect(Collectors.toList());

        PhotoCardList photoCardList = new PhotoCardList();
        photoCardList.setPhotocards(content);
        photoCardList.setPageNo(page.getNumber());
        photoCardList.setPageSize(page.getSize());
        photoCardList.setTotalElements(page.getTotalElements());
        photoCardList.setTotalPages(page.getTotalPages());
        photoCardList.setLast(page.isLast());

        return photoCardList;
    }

    @Override
    public PhotoCardDto getPhotocard(long id) {
        PhotoCard photoCard = getPCByID(id);
        return convertToDTO(photoCard);


    }

    @Override
    public PhotoCardDto updatePhotocard(PhotoCardDto photoCardDto, long pcID, long userID) {
        PhotoCard photoCard = getPCByID(pcID);
        User user = getUserByID(userID);
        if(photoCard.getOwner().getId() != user.getId()){
            throw new ResourceNotFoundException("photocard","pcID",pcID);
        }
        photoCard.setArtist(photoCardDto.getArtist());
        photoCard.setGroup(photoCardDto.getGroup());
        photoCard.setImgPath(photoCardDto.getImgPath());
        photoCard.setDateUpdated(LocalDateTime.now());

        PhotoCard updatedPC = photoCardRepository.save(photoCard);
        return convertToDTO(updatedPC);

    }

    @Override
    public void deletePhotocard(long id) {
        PhotoCard photoCard = getPCByID(id);
        photoCardRepository.delete(photoCard);
    }


    private PhotoCardDto convertToDTO(PhotoCard photoCard){
        PhotoCardDto photoCardDto = modelMapper.map(photoCard,PhotoCardDto.class);
        photoCardDto.setUserID(photoCard.getOwner().getId());
        return photoCardDto;
    }

    private PhotoCard convertToModel(PhotoCardDto photoCardDto){
//        PhotoCard photoCard = new PhotoCard();
        PhotoCard photoCard = modelMapper.map(photoCardDto,PhotoCard.class);
        User owner = getUserByID(photoCardDto.getUserID());
        photoCard.setOwner(owner);
//        photoCard.setImgPath(photoCardDto.getImgPath());
//        photoCard.setArtist(photoCardDto.getArtist());
//        photoCard.setGroup(photoCardDto.getGroup());
//        photoCard.setOwner(owner);


        return photoCard;
    }

    private PhotoCard getPCByID(long id){
        PhotoCard photoCard = photoCardRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("photocard","id",id));
        return photoCard;
    }

    private User getUserByID(long id){
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("user","id",id));
        return user;
    }
}
