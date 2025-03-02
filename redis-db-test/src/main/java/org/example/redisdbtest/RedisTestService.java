package org.example.redisdbtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 비즈니스 로직, 레디스 실질적 활용
 */
@Service
public class RedisTestService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate; // 이메일:문자열, 토큰:문자열

    public void store(String email, String token) {
        // 만료시간을 지정하여 데이터 제한을 둘수 있다
        redisTemplate.opsForValue().set(email, token, Duration.ofDays(7)); // 저장하는 토큰 7일간 유효
    }

    public String getTokenByEmail(String email) {
        // email -> 토큰 획득 -> 예외처리 생략
        // 키와 값이 명확하게 문자열로 오는것을 명시적으로 표현 (위에처럼 직접 접근 가능)
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        return opsForValue.get(email);
    }
}
