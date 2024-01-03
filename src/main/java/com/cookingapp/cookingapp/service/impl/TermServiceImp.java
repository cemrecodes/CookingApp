package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Term;
import com.cookingapp.cookingapp.repo.TermRepository;
import com.cookingapp.cookingapp.service.TermService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermServiceImp implements TermService {

    private final TermRepository termRepository;


    @Override
    public Term save(Term term) {
        return null;
    }

    @Override
    public void delete(Term term) {
        // todo
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
