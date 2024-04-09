package fr.eni.ecole.enchereseniprojetbackend.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class JwtResponse {
  private String token;
  @Getter
  @Setter
  private Long id;

  public JwtResponse(String accessToken, Long id) {
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
