package com.cookingapp.cookingapp.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberProfileResponse {
  private Long id;
  private String name;
  private String surname;
  private String email;
  private String profilePicUrl;
  private List<RecipeHeaderResponse> recipes;
}
