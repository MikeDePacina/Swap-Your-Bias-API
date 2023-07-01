package com.swapyourbias.Swap.Your.Bias.API.service;

import com.swapyourbias.Swap.Your.Bias.API.dto.PhotoCardDto;
import com.swapyourbias.Swap.Your.Bias.API.dto.PhotoCardList;

public interface PhotoCardService {
    PhotoCardDto postPhoto(PhotoCardDto photoCardDto);

    PhotoCardList getAllPhotocards(int pageNo, int pageSize, String sortBy, String sortOrder);

    PhotoCardDto getPhotocard(long id);

    PhotoCardDto updatePhotocard(PhotoCardDto photoCardDto, long pcID, long userID);

    void deletePhotocard(long id);
}
