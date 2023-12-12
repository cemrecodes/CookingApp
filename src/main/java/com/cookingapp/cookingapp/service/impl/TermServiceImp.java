package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Term;
import com.cookingapp.cookingapp.repo.TermRepository;
import com.cookingapp.cookingapp.service.TermService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermServiceImp implements TermService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TermServiceImp.class);

    private final TermRepository termRepository;


    @Override
    public Term save(Term term) {
        return null;
    }

    @Override
    public void delete(Term term) {

    }

    @Override
    public Term getByTerm(String term) {
        return null;
    }

    @Override
    public List<Term> getAllTerms() {
        return null;
    }
}
