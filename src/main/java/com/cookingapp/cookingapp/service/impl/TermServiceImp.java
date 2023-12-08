package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.repo.TermRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermServiceImp {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TermServiceImp.class);

    private final TermRepository termRepository;


}
