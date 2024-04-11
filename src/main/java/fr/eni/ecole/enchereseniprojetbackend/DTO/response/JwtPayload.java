package fr.eni.ecole.enchereseniprojetbackend.DTO.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class JwtPayload {
  private String token;

  @Getter
  @Setter
  private Long id;

  @Getter
  @Setter
  private boolean admin;

  public JwtPayload(String accessToken, Long id, boolean administrateur) {
    this.token = accessToken;
    this.id = id;
    this.admin = administrateur;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

}
