package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member save(Member member);
    void delete(Long id);
    List<Member> getAllMembers();
    Optional<Member> getMemberByEmail(String email);
    Optional<Member> getMemberById(Long memberId);
}
