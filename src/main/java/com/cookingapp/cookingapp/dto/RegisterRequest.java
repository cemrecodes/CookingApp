package com.cookingapp.cookingapp.dto;

import lombok.Data;

@Data
public class RegisterRequest {

  private String email;
  private String name;
  private String surname;
  private String password;

}