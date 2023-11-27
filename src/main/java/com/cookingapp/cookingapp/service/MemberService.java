package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    MemberDto save(MemberDto memberDto);
    void delete(Long id);
    List<MemberDto> getAll();
    Page<MemberDto> getAll(Pageable pageable);
}
