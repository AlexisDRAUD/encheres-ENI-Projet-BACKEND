package fr.eni.ecole.enchereseniprojetbackend.DTO.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class EnchereFormInput {

    @NotNull
    private LocalDateTime dateEnchere;

    @PositiveOrZero
    private long montantEnchere;

    @NotNull
    private Long userId;

    @NotNull
    private Long articleId;

}
