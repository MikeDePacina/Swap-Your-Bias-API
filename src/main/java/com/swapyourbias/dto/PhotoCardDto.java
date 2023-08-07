package com.swapyourbias.dto;

import com.swapyourbias.model.User;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor

public class PhotoCardDto {

    private long id;

    @NotBlank
    private String artist;

    private String group;

    //NotBlank, NotEmpty only for string NotNull works for object types
    private long userID;

    @NotBlank
    private String imgPath;

    private LocalDateTime datePosted;
    private LocalDateTime dateUpdated;
}
