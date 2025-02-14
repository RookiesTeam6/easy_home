package org.example.msasbuser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name="members")
@Data
@NoArgsConstructor
public class UserEntity implements UserDetails {
    // email -> primary key
    // username, password, address, role
    // enable (인증여부)
    @Id
    private String email;

    @Column(name="username")
    private String userName;

    private String password;
    private String address;
    private String roles;
    private boolean enable; // 이메일 인증 여부

    @Builder
    public UserEntity(String email, String userName, String password, String address, String roles, boolean enable) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.roles = roles;
        this.enable = enable;
    }

    // ----------------
    // UserDetails 파트 -> 6개 메소드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 역할 설정 => ROLE_USER
        return Collections.singletonList(new SimpleGrantedAuthority(roles));// roles 값을 그대로 사용
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return true;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    // 이메일 인증 여부를 체크
    @Override
    public boolean isEnabled() {return enable;}

    public String getUserName() {
        return userName;
    }
}
