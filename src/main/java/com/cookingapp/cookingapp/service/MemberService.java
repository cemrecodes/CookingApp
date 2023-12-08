package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Member;

import java.util.List;

public interface MemberService {
    Member save(Member member);
    void delete(Long id);
    List<Member> getAllMembers();
}
