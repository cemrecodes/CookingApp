package com.cookingapp.cookingapp.entity;

import com.cookingapp.cookingapp.dto.MemberDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String surname;
    private Long socialLoginId;
    private String profilePicUrl;
    private String password;

    @Enumerated
    private MemberRole memberRole;

    @Enumerated
    private LoginType socialTypeLogin;

    private Boolean locked;
    private Boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(memberRole.name());
        return Collections.singletonList(authority);
    }


    // kullanılmıyor
    @Override
    public String getUsername() {
        return email;
    }

    // kullanılmıyor
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public enum LoginType {
        GOOGLE,
        FACEBOOK,
        NONE
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public MemberDto toDto(){
        MemberDto memberDto = new MemberDto();
        memberDto.setId(id);
        memberDto.setName(name);
        memberDto.setSurname(surname);
        memberDto.setProfilePicUrl(profilePicUrl);
        memberDto.setEmail(email);
        return memberDto;
    }


}
