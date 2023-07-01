package com.swapyourbias.Swap.Your.Bias.API.dto;

import com.swapyourbias.Swap.Your.Bias.API.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
