package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Term;
import com.cookingapp.cookingapp.repo.TermRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;


    
    public Term save(Term term) {
        return null;
    }

    
    public void delete(Term term) {
        // todo
    }

    
    public Term getByTerm(String term) {
        return null;
    }

    
    public List<Term> getAllTerms() {
        return null;
    }
}
