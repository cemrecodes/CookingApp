package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.service.MemberService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final MemberService memberService;
  private final AuthenticationManager authenticationManager;

  public Optional<Member> login(String email, String password){
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              email,
              password
          )
      );
    } catch (BadCredentialsException exception) {
      return Optional.empty();
    }
    return memberService.findMemberByEmail(email);
  }

}
