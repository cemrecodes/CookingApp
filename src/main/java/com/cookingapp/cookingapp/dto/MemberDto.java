package com.cookingapp.cookingapp.dto;

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
}
