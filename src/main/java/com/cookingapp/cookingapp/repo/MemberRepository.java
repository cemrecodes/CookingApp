package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

  Optional<Member> findById(Long id);

  UserDetails findMemberByEmail(String email);

}
