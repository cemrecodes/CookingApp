package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.dto.RegistrationRequest;
import com.cookingapp.cookingapp.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

  private  final MemberService memberService;
  public String register(@RequestBody RegistrationRequest request){
    // return memberService.register(request);
    return null;
  }



}
