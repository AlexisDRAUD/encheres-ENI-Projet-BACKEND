package fr.eni.ecole.enchereseniprojetbackend.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PasswordDto {

    @NotNull
    private String password;

    @NotNull
    private String passwordConfirmation;

    @NotNull
    private  String token;

}