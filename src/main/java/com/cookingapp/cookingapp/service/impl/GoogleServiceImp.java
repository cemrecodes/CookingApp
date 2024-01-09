package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.dto.MemberDto;
import com.cookingapp.cookingapp.entity.Member.LoginType;
import com.cookingapp.cookingapp.service.GoogleService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GoogleServiceImp implements GoogleService {

  @Value("${spring.auth.google.client-id}")
  private String CLIENT_ID;

  public Boolean verifyToken(String token) {
    return verifyIdToken(token) != null;
  }
  public GoogleIdToken verifyIdToken(String idTokenString) {
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
        new NetHttpTransport(), GsonFactory.getDefaultInstance()
    )
        .setAudience(Collections.singletonList(CLIENT_ID))
        .build();

    try {
      return verifier.verify(idTokenString);
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public MemberDto getGoogleAuthMemberInfo(GoogleIdToken googleIdToken){
    MemberDto memberDto = new MemberDto();
    Payload payload = googleIdToken.getPayload();

    // Print user identifier
    String userId = payload.getSubject();
    System.out.println("User ID: " + userId);

    // Get profile information from payload
    String email = payload.getEmail();
    boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
    String name = (String) payload.get("name");
    String pictureUrl = (String) payload.get("picture");
    String locale = (String) payload.get("locale");
    String familyName = (String) payload.get("family_name");
    String givenName = (String) payload.get("given_name");

    memberDto.setName((String) payload.get("given_name"));
    memberDto.setSurname((String) payload.get("family_name"));
    memberDto.setEmail(payload.getEmail());
    // memberDto.setSocialLoginId(Long.parseLong(payload.getSubject()));
    memberDto.setProfilePicUrl((String) payload.get("picture"));
    memberDto.setSocialTypeLogin(String.valueOf(LoginType.GOOGLE));
    memberDto.setRole("USER");

    return memberDto;
  }

  public String getEmail(GoogleIdToken googleIdToken){
    return googleIdToken.getPayload().getEmail();
  }

}
