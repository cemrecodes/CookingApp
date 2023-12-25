package com.cookingapp.cookingapp.dto;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Member.LoginType;
import com.cookingapp.cookingapp.entity.MemberRole;
import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private Long socialLoginId;
    private String profilePicUrl;
    private String socialTypeLogin;
    private String role;

    public Member convertToMember(){
        Member member = new Member();
        member.setEmail(email);
        member.setName(name);
        member.setSurname(surname);
        member.setSocialLoginId(socialLoginId);
        member.setProfilePicUrl(profilePicUrl);
        member.setSocialTypeLogin(LoginType.valueOf(socialTypeLogin));
        member.setLocked(false);
        member.setEnabled(true);
        member.setMemberRole(MemberRole.valueOf(role));
        return member;
    }
}
