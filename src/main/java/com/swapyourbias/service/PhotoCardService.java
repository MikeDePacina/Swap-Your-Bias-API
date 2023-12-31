package com.swapyourbias.service;

import com.swapyourbias.dto.PhotoCardDto;
import com.swapyourbias.dto.PhotoCardList;
import com.swapyourbias.model.PhotoCard;

public interface PhotoCardService {
    PhotoCardDto postPhoto(PhotoCardDto photoCardDto);

    PhotoCardList getAllPhotocards(int pageNo, int pageSize, String sortBy, String sortOrder);

    PhotoCardDto getPhotocard(long id);

    PhotoCardDto updatePhotocard(PhotoCardDto photoCardDto, long pcID, long userID);

    void deletePhotocard(long id);

    PhotoCard convertToModel(PhotoCardDto photoCardDto);

    PhotoCardDto convertToDTO(PhotoCard photoCard);
}
