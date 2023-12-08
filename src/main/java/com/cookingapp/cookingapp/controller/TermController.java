package com.cookingapp.cookingapp.controller;


import com.cookingapp.cookingapp.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/terms")
@RequiredArgsConstructor
public class TermController {

    // private final TermService termService;


}
