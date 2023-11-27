package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.dto.MemberDto;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.repo.MemberRepository;
import com.cookingapp.cookingapp.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImp implements MemberService {

    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public MemberDto save(MemberDto memberDto) {
        Member member = new Member();
        member.setEmail(memberDto.getEmail());
        member.setName(memberDto.getName());
        member.setSurname(memberDto.getSurname());
        member.setSocialLoginId(memberDto.getSocialLoginId());
        member.setProfilePicUrl(memberDto.getProfilePicUrl());

        if(memberDto.getSocialTypeLogin().equals("GOOGLE")) {
            member.setSocialTypeLogin(Member.LoginType.GOOGLE);
        }
        else if(memberDto.getSocialTypeLogin().equals("FACEBOOK")) {
            member.setSocialTypeLogin(Member.LoginType.FACEBOOK);
        }
        else if(memberDto.getSocialTypeLogin().equals("NONE")) {
            member.setSocialTypeLogin(Member.LoginType.NONE);
        }

        memberRepository.save(member);
        return memberDto;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<MemberDto> getAll() {
        List<Member> members = memberRepository.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        members.forEach(it -> {
                    MemberDto memberDto = new MemberDto();
                    memberDto.setId(it.getId());
                    memberDtos.add(memberDto);
                }
        );
        return memberDtos;
    }

    @Override
    public Page<MemberDto> getAll(Pageable pageable) {
        return null;
    }
}
