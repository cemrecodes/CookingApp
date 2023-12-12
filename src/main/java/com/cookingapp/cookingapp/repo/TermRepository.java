package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    Term save(Term term);
    void delete(Term term);
    Term getByTerm(String word);
    Term getById(Long id);
    List<Term> findAll();

}
