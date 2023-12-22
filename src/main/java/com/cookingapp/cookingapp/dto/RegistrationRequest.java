package com.cookingapp.cookingapp.dto;

import lombok.Data;

@Data
public class RegistrationRequest {
  private String name;
  private String surname;
  private String email;
  private String password;
}
