package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.MemberDto;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;


public interface GoogleService {
  Boolean verifyToken(String token);
  GoogleIdToken verifyIdToken(String idTokenString);

  MemberDto getGoogleAuthMemberInfo(GoogleIdToken googleIdToken);

  String getEmail(GoogleIdToken googleIdToken);
}
