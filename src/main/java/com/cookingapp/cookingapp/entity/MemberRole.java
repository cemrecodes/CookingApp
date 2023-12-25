package com.cookingapp.cookingapp.entity;

public enum MemberRole {
  USER,
  ADMIN;

  public MemberRole toMemberRole(String memberRole){
    switch(memberRole){
      case "USER":
        return USER;
      case "ADMIN":
        return ADMIN;
    }
    return null;
  }

  public String toString(MemberRole role){
    return switch (role) {
      case USER -> "USER";
      case ADMIN-> "ADMIN";
      default -> null;
    };
  }
}
