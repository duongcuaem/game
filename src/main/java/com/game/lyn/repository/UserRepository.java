package com.game.lyn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.game.lyn.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Kiểm tra xem username đã tồn tại hay chưa
    boolean existsByUsername(String username);

    // Tìm người dùng theo username
    Optional<User> findByUsername(String username);

    // Tìm người dùng theo id
    Optional<User> findById(Long id);

}
