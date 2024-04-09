package fr.eni.ecole.enchereseniprojetbackend.Security;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Setter;

public class SignupRequest {
    @Setter
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @Setter
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> roles;

    @Setter
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public void setRole(Set<String> roles) {
        this.roles = roles;
    }
}