package com.swapyourbias.controller;

import com.swapyourbias.dto.PhotoCardDto;
import com.swapyourbias.dto.PhotoCardList;
import com.swapyourbias.service.PhotoCardService;
import com.swapyourbias.utils.APIConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/photocards")
public class PhotoCardController {

    private PhotoCardService photoCardService;

    public PhotoCardController(PhotoCardService photoCardService) {
        this.photoCardService = photoCardService;
    }


    @GetMapping
    public PhotoCardList getAllPhotocards(
            @RequestParam(name = "pageNo", defaultValue = APIConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = APIConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = APIConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = APIConstants.DEFAULT_SORT_ORDER, required = false) String sortOrder
            ){

        return photoCardService.getAllPhotocards(pageNo,pageSize,sortBy,sortOrder);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoCardDto> getPhotocard(@PathVariable(name = "id") long id ){
        return new ResponseEntity<PhotoCardDto>(photoCardService.getPhotocard(id), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<PhotoCardDto> postPhotoCard(@Valid @RequestBody PhotoCardDto photoCardDto){
           return new ResponseEntity<PhotoCardDto>(photoCardService.postPhoto(photoCardDto), HttpStatus.CREATED);

    }



    @PutMapping("/{userID}/{pcID}")
    public ResponseEntity<PhotoCardDto> updatePhotoCard(@Valid @RequestBody PhotoCardDto photoCardDto,
                                                        @PathVariable(name = "userID") long userID,
                                                        @PathVariable(name = "pcID") long pcID){
        return new ResponseEntity<PhotoCardDto>(photoCardService.updatePhotocard(photoCardDto,pcID,userID), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhotoCard(@PathVariable(name = "id") long id){
        photoCardService.deletePhotocard(id);
        return new ResponseEntity<String>("Deleted",HttpStatus.OK);
    }
}
