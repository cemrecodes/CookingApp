package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Term;

import java.util.List;


public interface TermService {

    Term save(Term term);

    void delete(Term term);

    Term getByWord(String word);

    List<Term> getAllTerms();
    
}
