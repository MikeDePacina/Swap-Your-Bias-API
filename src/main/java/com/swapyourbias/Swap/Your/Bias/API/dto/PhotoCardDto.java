package com.swapyourbias.Swap.Your.Bias.API.dto;

import com.swapyourbias.Swap.Your.Bias.API.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class PhotoCardDto {
    @NotBlank
    private long id;

    @NotBlank
    private String artist;

    private String group;

    @NotBlank
    private User owner;

    private LocalDateTime datePosted;
    private LocalDateTime dateUpdated;
}
