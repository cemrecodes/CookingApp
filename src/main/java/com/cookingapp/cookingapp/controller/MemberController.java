package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.dto.MemberDto;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.service.GoogleService;
import com.cookingapp.cookingapp.service.MemberService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final GoogleService googleService;

    @GetMapping(value = "/{token}")
    public ResponseEntity veriftToken(@PathVariable String token) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.googleService.verifyIdToken(token));
    }


    // TODO DTO DÖNECEK
    @GetMapping
    public ResponseEntity<List<Member>> getAll(){
        return ResponseEntity.ok(memberService.getAllMembers());
    }
}
