package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.MemberDto;
import com.cookingapp.cookingapp.entity.Member.LoginType;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class GoogleService {

  @Value("${spring.auth.google.client-id}")
  private String CLIENT_ID;

  public GoogleIdToken verifyIdToken(String idTokenString)
      throws IOException, GeneralSecurityException {
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
        new NetHttpTransport(), GsonFactory.getDefaultInstance()
    )
        .setAudience(Collections.singletonList(CLIENT_ID))
        .build();

    return verifier.verify(idTokenString);
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
    memberDto.setSocialLoginId(Long.valueOf(payload.getSubject()));
    memberDto.setProfilePicUrl((String) payload.get("picture"));
    memberDto.setSocialTypeLogin(String.valueOf(LoginType.GOOGLE));

    return memberDto;
  }

  /*
  GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
      // Specify the CLIENT_ID of the app that accesses the backend:
      .setAudience(Collections.singletonList(CLIENT_ID))
      // Or, if multiple clients access the backend:
      //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
      .build();

// (Receive idTokenString by HTTPS POST)

  GoogleIdToken idToken = verifier.verify(idTokenString);
if (idToken != null) {
    Payload payload = idToken.getPayload();

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

    // Use or store profile information
    // ...

  } else {
    System.out.println("Invalid ID token.");
  }

   */
}
