package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.config.JwtService;
import com.cookingapp.cookingapp.dto.MemberDto;
import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.response.MemberProfileResponse;
import com.cookingapp.cookingapp.response.RecipeHeaderResponse;
import com.cookingapp.cookingapp.service.GoogleService;
import com.cookingapp.cookingapp.service.MemberService;
import com.cookingapp.cookingapp.service.RecipeMemberService;
import com.cookingapp.cookingapp.service.impl.MemberServiceImp;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MemberServiceImp memberServiceImp;

    private final RecipeMemberService recipeMemberService;

    private final GoogleService googleService;

    private final JwtService jwtService;

    /*
    @GetMapping(value = "/{token}")
    public ResponseEntity verifyToken(@PathVariable String token) {
        return ResponseEntity.ok(this.googleService.verifyIdToken(token));
    }

     */

    @PostMapping(value = "/login/google")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> requestBody){
        String idToken = requestBody.get("idToken");
        log.info("Request: {}" , idToken);
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
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping
    public ResponseEntity<MemberProfileResponse> getMember(){
        log.info("getMember has been called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            Optional<Member> member = memberService.getMemberByEmail(email);
            if (member.isPresent()) {
                MemberProfileResponse profileResponse = new MemberProfileResponse();
                List<RecipeHeaderResponse> recipeList = recipeMemberService.getRecipesByMember(member.get())
                    .stream()
                    .map(Recipe::toHeaderResponse)
                    .toList();
                profileResponse.setId(member.get().getId());
                profileResponse.setName(member.get().getName());
                profileResponse.setSurname(member.get().getSurname());
                profileResponse.setProfilePicUrl(member.get().getProfilePicUrl());
                profileResponse.setEmail(member.get().getEmail());
                profileResponse.setRecipes(recipeList);
                log.info("Response: {}", profileResponse);
                return ResponseEntity.ok(profileResponse);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{memberId}")

    public ResponseEntity<MemberProfileResponse> getMemberById(@PathVariable Long memberId){
        log.info("getMemberById has been called with member id: {}", memberId);
        Optional<Member> member = memberService.getMemberById(memberId);
        if(member.isPresent()){
            MemberProfileResponse profileResponse = new MemberProfileResponse();
            List<RecipeHeaderResponse> recipeList = recipeMemberService.getRecipesByMemberId(memberId)
                .stream()
                .map(Recipe::toHeaderResponse)
                .toList();
            profileResponse.setId(memberId);
            profileResponse.setName(member.get().getName());
            profileResponse.setProfilePicUrl(member.get().getProfilePicUrl());
            profileResponse.setSurname(member.get().getSurname());
            profileResponse.setRecipes(recipeList);
            return ResponseEntity.ok(profileResponse);
        }

        return ResponseEntity.notFound().build();
    }
    /*
    @GetMapping
    public ResponseEntity<List<Member>> getAll(){
        return ResponseEntity.ok(memberService.getAllMembers());
    }

     */

    @GetMapping("/{memberId}/recipes")
    public ResponseEntity<List<RecipeDto>> getRecipesByMember(@PathVariable Long memberId){
        log.info("getRecipesByMember has been called with memberId: {}", memberId);
        List<RecipeDto> recipeDtoList = recipeMemberService.getRecipesByMemberId(memberId).stream().map(Recipe::toDto).toList();
        return ResponseEntity.ok(recipeDtoList);
    }

}
