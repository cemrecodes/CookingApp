package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.dto.LoginRequest;
import com.cookingapp.cookingapp.dto.RegisterRequest;
import com.cookingapp.cookingapp.entity.Member.LoginType;
import com.cookingapp.cookingapp.entity.MemberRole;
import com.cookingapp.cookingapp.security.JwtService;
import com.cookingapp.cookingapp.dto.MemberDto;
import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.response.MemberProfileResponse;
import com.cookingapp.cookingapp.response.RecipeHeaderResponse;
import com.cookingapp.cookingapp.service.GoogleService;
import com.cookingapp.cookingapp.service.MemberService;
import com.cookingapp.cookingapp.service.RecipeMemberService;
import com.cookingapp.cookingapp.service.impl.AuthenticationService;
import com.cookingapp.cookingapp.service.impl.MemberServiceImp;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MemberServiceImp memberServiceImp;

    private final RecipeMemberService recipeMemberService;

    private final GoogleService googleService;

    private final JwtService jwtService;

    private final AuthenticationService authService;

    /*
    @GetMapping(value = "/{token}")
    public ResponseEntity verifyToken(@PathVariable String token) {
        return ResponseEntity.ok(this.googleService.verifyIdToken(token));
    }
     */

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){
        log.info("Register request has been received.");
        if(memberService.findMemberByEmail(registerRequest.getEmail()).isPresent()){
            // todo general entity for error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is a user registered with this email already");
        }

        Member member = Member.builder().email(registerRequest.getEmail())
            .memberRole(MemberRole.USER)
            .name(registerRequest.getName())
            .surname(registerRequest.getSurname())
            .socialTypeLogin(LoginType.INTERNAL)
            .password(registerRequest.getPassword())
            .locked(false)
            .enabled(true)
            .build();
        Member savedMember = memberService.save(member);
        if( savedMember != null ) {
            return ResponseEntity.status(HttpStatus.OK).body("Successfully registered.");
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest){
        Optional<Member> member = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if( member.isPresent() ){
            return ResponseEntity.ok(Map.of("token", jwtService.generateToken(member.get())));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @PostMapping(value = "/login/google")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> requestBody){
        String idToken = requestBody.get("idToken");
        log.info("Google Login Request has been received.");
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
        // todo new auth (shorter)
        Member member = authService.isAuthenticated();
        if (member != null) {
                MemberProfileResponse profileResponse = new MemberProfileResponse();
                List<RecipeHeaderResponse> recipeList = recipeMemberService.getRecipesByMember(member)
                    .stream()
                    .map(Recipe::toHeaderResponse)
                    .toList();
                profileResponse.setId(member.getId());
                profileResponse.setName(member.getName());
                profileResponse.setSurname(member.getSurname());
                profileResponse.setProfilePicUrl(member.getProfilePicUrl());
                profileResponse.setEmail(member.getEmail());
                profileResponse.setRecipes(recipeList);
                return ResponseEntity.ok(profileResponse);
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
