package org.example.msasbuser.repository;

import org.example.msasbuser.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 커스텀 -> email 검색
    Optional<UserEntity> findByEmail(String email);
}
