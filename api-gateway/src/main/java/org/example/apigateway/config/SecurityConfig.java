package org.example.apigateway.config;

import org.example.apigateway.handler.CustomAccessDeniedHandler;
import org.example.apigateway.handler.CustomAuthenticationEntryPoint;
import org.example.apigateway.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity // 보안 설정 어노테이션
public class SecurityConfig {
    // 생성자 방식 DI
    private final JwtFilter jwtFilter;
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    // 0. spring webflux 보안 구성에 대한 설정
    //    요청이 들어오면 -> 게이트웨이에서 받아서 -> 1차 보안 점검 -> 이상없으면 개별 서비스로 전달
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // http 설정
        http
                // CORS 설정 -> 기본값 적용
                .cors(Customizer.withDefaults())
                // CSRF 공격에 대한 보호 설정 -> 비활성 처리
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // iframe 허용에 대한 설정
                // X-Frame-Options HTTP 헤더에 대한 비활성화 처리 -> iframe 허용
                .headers( header -> header.frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable) )

                // 보안 컨텍스트 저정소 활성화 부분 -> 레디스 사용
                // 인증, 세션관리를 스프링에서 처리하지 않고, 별도로 관리한다면 -> 옵션 적용 가능
                // Context -> 세션을 보관하는 곳
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

                // 인증 없이 접근 가능한 페이지 (회원가입, 로그인, 기타 서비스별 별도 페이지들 가능)
                .authorizeExchange( authorizeExchangeSpec -> authorizeExchangeSpec
                        // 인증없이 접근 가능
                        .pathMatchers("/",
                                // 로그인
                                "/auth/login",
                                // 회원가입
                                "/user/signup",
                                // 이메일 인증
                                "/user/valid"
                        ).permitAll()

                        // 개별 서비스별 URL
                        // 🏡 [입주민 전용] resident/** 페이지 -> ADMIN은 접근 불가
                        .pathMatchers("/resident/**").permitAll()

                        // 📝 [게시판] 입주민만 작성, 수정, 삭제 가능 / 관리자는 읽기만 가능
                        .pathMatchers(HttpMethod.GET, "/board/**").permitAll()  // 누구나 읽기 가능
                        .pathMatchers(HttpMethod.POST, "/board/**").hasRole("USER")  // 입주민만 작성 가능
                        .pathMatchers(HttpMethod.PUT, "/board/**").hasRole("USER")   // 입주민만 수정 가능
                        .pathMatchers(HttpMethod.DELETE, "/board/**").hasRole("USER") // 입주민만 삭제 가능

                        // 🏡 [관리자 전용] admin/** 페이지 -> USER는 접근 불가
                        .pathMatchers("/admin/**").hasRole("ADMIN")  // 관리자 권한 필요

                        // 📢 [공지사항] 관리자만 작성, 수정, 삭제 가능
                        .pathMatchers(HttpMethod.GET, "/notification/**").permitAll() // 모두 읽기 가능
                        .pathMatchers(HttpMethod.POST, "/notification/**").hasRole("ADMIN") // 관리자만 작성 가능
                        .pathMatchers(HttpMethod.PUT, "/notification/**").hasRole("ADMIN") // 관리자만 수정 가능
                        .pathMatchers(HttpMethod.DELETE, "/notification/**").hasRole("ADMIN") // 관리자만 삭제 가능

                        // 그 외 모든 요청은 인증 필요
                        .anyExchange().authenticated()
                )

                // 나머지 API들은 인증해야 가능
                // 간편한 예외처리 -> 오류 처리를 위한 설정(인증 -> 로그인없이 서비스 접근 / 권한 -> 관리자가 아닌데 관리자 메뉴 진입)
                // 인증 오류 -> 401, 권한 오류 403 자동 처리, 필요시 추가 가능
                .exceptionHandling(exception -> {
                    exception
                            .accessDeniedHandler(new CustomAccessDeniedHandler())            // 403
                            .authenticationEntryPoint(new CustomAuthenticationEntryPoint()); // 401
                })
                // JWT 인증 처리 -> 필터 등록을 통해서 해결
                // 요청에서 JWT 처리 반영, 토큰 확인, 검증하는 필터로 사용 (본격적 요청이 처리되기 전)
                // 인증처리를 해당 필터를 통해서 진행한다!
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHORIZATION);

        return http.build();
    }

    // 1. 암호화처리 -> 비번 처리 (필요시, 마스킹 정보들 추가)
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
