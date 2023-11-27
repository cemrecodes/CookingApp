package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.dto.MemberDto;
import com.cookingapp.cookingapp.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberDto>> getAll(){
        return ResponseEntity.ok(memberService.getAll());
    }
}
