package org.example.msasbuser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        // 인증 없이 접근 가능한 API
                        .requestMatchers("/auth/**", "/user/signup", "/user/valid","/user/mypage").permitAll()

                        // 🏡 [입주민 전용] resident/** -> ADMIN은 접근 불가
                        .requestMatchers("/resident/**").hasRole("USER")

                        // 📝 [게시판] 입주민만 작성, 수정, 삭제 가능 / 관리자는 읽기만 가능
                        .requestMatchers(HttpMethod.GET, "/board/**").permitAll()  // 누구나 읽기 가능
                        .requestMatchers(HttpMethod.POST, "/board/**").hasRole("USER")  // 입주민만 작성 가능
                        .requestMatchers(HttpMethod.PUT, "/board/**").hasRole("USER")   // 입주민만 수정 가능
                        .requestMatchers(HttpMethod.DELETE, "/board/**").hasRole("USER") // 입주민만 삭제 가능

                        // 🏡 [관리자 전용] admin/** -> USER는 접근 불가
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // 관리자 권한 필요

                        // 📢 [공지사항] 관리자만 작성, 수정, 삭제 가능
                        .requestMatchers(HttpMethod.GET, "/notification/**").permitAll() // 모두 읽기 가능
                        .requestMatchers(HttpMethod.POST, "/notification/**").hasRole("ADMIN") // 관리자만 작성 가능
                        .requestMatchers(HttpMethod.PUT, "/notification/**").hasRole("ADMIN") // 관리자만 수정 가능
                        .requestMatchers(HttpMethod.DELETE, "/notification/**").hasRole("ADMIN") // 관리자만 삭제 가능

                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안 함 (JWT 인증 방식)
                .formLogin(AbstractHttpConfigurer::disable); // 로그인 화면 비활성


        return http.build();
    }
}
