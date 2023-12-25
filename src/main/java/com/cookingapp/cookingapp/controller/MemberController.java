package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.config.JwtService;
import com.cookingapp.cookingapp.dto.MemberDto;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.service.GoogleService;
import com.cookingapp.cookingapp.service.MemberService;
import com.cookingapp.cookingapp.service.impl.MemberServiceImp;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;

    private final MemberServiceImp memberServiceImp;

    private final GoogleService googleService;

    private final JwtService jwtService;

    @GetMapping(value = "/{token}")
    public ResponseEntity verifyToken(@PathVariable String token) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.googleService.verifyIdToken(token));
    }

    @PostMapping(value = "/login/google")
    public ResponseEntity login(@RequestBody Map<String, String> requestBody){
        String idToken = requestBody.get("idToken");
        logger.info("Request: {}" , idToken);
        GoogleIdToken googleIdToken = googleService.verifyIdToken(idToken);
        if( googleIdToken != null ){
            // Optional<Member> member = memberService.getMemberByEmail(googleService.getEmail(googleIdToken));
            UserDetails member = memberServiceImp.loadUserByUsername(googleService.getEmail(googleIdToken));
            if(member != null){
                return ResponseEntity.ok(Map.of("token", jwtService.generateToken(member)));
            }
            else{
                MemberDto memberDto = googleService.getGoogleAuthMemberInfo(googleIdToken);
                member = memberService.save(memberDto.convertToMember());
                return ResponseEntity.ok(Map.of("token", jwtService.generateToken(member)));
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }



    @GetMapping
    public ResponseEntity<List<Member>> getAll(){
        return ResponseEntity.ok(memberService.getAllMembers());
    }
}
