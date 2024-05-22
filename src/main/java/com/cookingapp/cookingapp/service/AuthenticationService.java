package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

  public Member isAuthenticated() {
    //String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal() ??
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
      String email = userDetails.getUsername();
      Optional<Member> member = memberService.getMemberByEmail(email);
      if (member.isPresent()) {
        return member.get();
      }
    }
    return null;
  }

}
