package org.example.redisdbtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 테스트용 요청 라우트 처리, 데이터 저장, 데이터 추출
 */
@RestController
@RequestMapping("/redis")
public class RedisTestController {
    @Autowired
    private RedisTestService redisTestService;

    // 데이터 추가
    @PostMapping("/store")
    public ResponseEntity<String> store(@RequestParam("email") String email) {
        System.out.println("이메일을 레디스에 저장");
        String token = UUID.randomUUID().toString(); // 랜덤으로 기기에서 토큰 생성
        // 레디스에 저장
        redisTestService.store(email, token);
        // 응답
        return ResponseEntity.ok("저장 테스트 완료");
    }

    // 데이터 조회 -> GET,email값 전달 추출
    //메소드명 getTokenByEmail
    @GetMapping("/detail")
    public ResponseEntity<String> detail(@RequestParam("email") String email) {
        return ResponseEntity.ok("토큰 : " + redisTestService.getTokenByEmail(email));
    }
}
