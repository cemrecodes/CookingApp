package com.cookingapp.cookingapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstructionDto {
    private String instruction;
    private String time;

}
