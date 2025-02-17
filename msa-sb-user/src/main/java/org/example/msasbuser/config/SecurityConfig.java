package org.example.msasbuser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 게이트웨이에서 모드 걸러낸 요청만 진입 -> 모든 요청 통과
 */

// MVC 서비스로 변경
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 접근 가능한 API (회원가입, 로그인, 이메일 인증)
                        .requestMatchers("/auth/**", "/user/signup", "/user/valid","/user/mypage").permitAll()

                        // 🏠 [마이페이지] 로그인한 사용자만 접근 가능
//                        .requestMatchers("/user/mypage").authenticated()

                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안 함 (JWT 인증 방식)
                .formLogin(AbstractHttpConfigurer::disable); // 로그인 화면 비활성


        return http.build();
    }
}
