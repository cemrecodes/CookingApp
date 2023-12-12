package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Term;

import java.util.List;


public interface TermService {

    Term save(Term term);

    void delete(Term term);

    Term getByTerm(String term);

    List<Term> getAllTerms();
    
}
