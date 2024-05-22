package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.repo.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final static String USER_NOT_FOUND_MESSAGE = "Member (user) with email %s not found";

    /*
    
    @Transactional
    public Member save(MemberDto memberDto) {
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

     */

    
    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    
    public void delete(Long id) {

    }

    
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    
    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    
    public Optional<Member> getMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    
    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email).orElse(null);
          //  .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));
    }
}
