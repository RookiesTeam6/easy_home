package org.example.msasbuser.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.msasbuser.dto.SignUpEventDto;
import org.example.msasbuser.dto.UserDeleteEventDto;
import org.example.msasbuser.dto.UserDto;
import org.example.msasbuser.dto.UserUpdateDto;
import org.example.msasbuser.jwt.JwtTokenProvider;
import org.example.msasbuser.kafka.KafkaProducer;
import org.example.msasbuser.service.AddressService;
import org.example.msasbuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;

/**
 * 회원가입, 회원정보수정, 비번수정,...
 */
@RestController
// 게이트웨이에서 연동되어 있음
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private KafkaProducer kafkaProducer;

    // 에코테스트
    @GetMapping("/echo")
    public ResponseEntity<String> echo() {
        return ResponseEntity.ok("/user~ 에코 테스트");
    }


    // 회원가입
    // post, json 데이터 전달 -> 엔티티, dto 고려하여 구성
    @PostMapping("/signup")
    // ResponseEntity<?>  => 응답을 유연하게 구성 가능함
    public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
        System.out.println("회원가입요청 : " + userDto.toString());

        // 주소 검색
        if (userDto.getAddress() == null || userDto.getAddress().isEmpty()) {
            throw new IllegalArgumentException("주소를 입력해야 합니다.");
        }
        // 주소 검색 API 호출
        String fullAddress = addressService.searchAddress(userDto.getAddress());
        userDto.setAddress(fullAddress);


        // 1. 회원가입처리 -> 비즈니스로직 -> 서비스 해결
        // 2. UserSercvice에 createUser( xxDTO ) -> 레퍼지토리 -> 디비까지 입력 구성 -> 인증메일발송
        userService.createUser( userDto );

        // 3. 회원가입 이벤트 발송
        SignUpEventDto signUpEventDto = SignUpEventDto.builder()
                .email(userDto.getEmail())
                .nickname(userDto.getUserName())
                .build();


        try {
            kafkaProducer.sendMsg("user-signup-topic", signUpEventDto); // Kafka Producer를 통해 이벤트 전송
            System.out.println("회원가입 이벤트 전송 : " + signUpEventDto);
        } catch (JsonProcessingException e) {
            System.err.println("회원가입 이벤트 전송 실패 : " + e.getMessage());
            return ResponseEntity.status(500).body("이벤트 발송 중 오류가 발생했습니다.");
        }

        // 4. 응답처리
        return ResponseEntity.ok("회원가입 성공");
    }


    // 이메일 검증 처리 -> 인증메일 처리 -> enable의 값을 f->t
    // GET, /valid, 엑세스토큰없이 전근가능해야함(로그인 전->게이트웨이 수정), 파라미터 token, 입주민 테이블 업데이트
    @GetMapping("/valid")
    public ResponseEntity<String> valid(@RequestParam ("token") String token) {
        try{
            // 입주민 테이블 업데이트
            userService.updateActivate(token);
            return ResponseEntity.ok("이메일 인증 완료. 계정이 활성화 되었습니다.");

        } catch (IllegalArgumentException e) {
            //조작된(만료된) 토큰을 인증 -> 비정상, Bad Request
            return ResponseEntity.status(500).body("서버측 내부 오류 : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버측 내부 오류 : " + e.getMessage());
        }
    }

    // 마이페이지 조회
    @GetMapping("/mypage")
    public ResponseEntity<UserDto> getMyPage(@RequestHeader("Authorization") String accessToken) {
        // JWT에서 이메일 추출
        String email = jwtTokenProvider.extractEmail(accessToken);
        System.out.println("현재 로그인한 사용자 이메일: " + email);

        if (email == null) {
            return ResponseEntity.status(403).build(); // 인증 실패
        }

        // 이메일로 유저 정보 조회
        UserDto userDto = userService.getUserInfoByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    // 마이페이지 수정 및 업데이트
    @PutMapping("/mypage")
    public ResponseEntity<String> updateMyPage(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody UserUpdateDto userUpdateDto) {

        // JWT에서 이메일 추출
        String email = jwtTokenProvider.extractEmail(accessToken);

        if (email == null) {
            return ResponseEntity.status(403).body("인증 실패");
        }

        try {
            // 유저 정보 업데이트 및 응답 메시지 반환
            String message = userService.updateUserInfo(email, userUpdateDto);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String accessToken) {
        // JWT에서 이메일 추출
        String email = jwtTokenProvider.extractEmail(accessToken);
        System.out.println("현재 로그인한 사용자 이메일: " + email);

        if (email == null) {
            return ResponseEntity.status(403).body("인증 실패");
        }

        // 사용자 탈퇴 처리
        try {
            userService.deleteUserByEmail(email); // 이메일을 기반으로 사용자 정보 삭제

            // 탈퇴 이벤트 발송 (닉네임은 "탈퇴한 사용자"로 설정)
            UserDeleteEventDto userDeleteEventDto = UserDeleteEventDto.builder()
                    .email(email)
                    .nickname("탈퇴한 사용자") // 탈퇴한 사용자의 닉네임
                    .build();

            // Kafka Producer를 통해 탈퇴 이벤트 전송
            kafkaProducer.sendMsg("user-deletion-topic", userDeleteEventDto); // Kafka Producer를 통해 탈퇴 이벤트 전송
            System.out.println("회원 탈퇴 이벤트 전송: " + userDeleteEventDto);

            // 회원 탈퇴 성공 응답 반환
            return ResponseEntity.ok("회원 탈퇴 성공");
        } catch (IllegalArgumentException e) {
            // 사용자를 찾을 수 없는 경우 404 응답 반환
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        } catch (JsonProcessingException e) {
            // Kafka 메시지 전송 중 JSON 변환 오류 발생 시 500 응답 반환
            System.err.println("탈퇴 이벤트 전송 실패 : " + e.getMessage());
            return ResponseEntity.status(500).body("이벤트 발송 중 오류가 발생했습니다.");
        }
    }
}
