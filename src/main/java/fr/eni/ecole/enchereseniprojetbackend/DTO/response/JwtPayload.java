package fr.eni.ecole.enchereseniprojetbackend.DTO.response;

import lombok.Getter;
import lombok.Setter;

public class JwtPayload {
  private String token;
  @Getter
  @Setter
  private Long id;

  public JwtPayload(String accessToken, Long id) {
    this.token = accessToken;
    this.id = id;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

}
