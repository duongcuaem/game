package com.game.lyn.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.game.lyn.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Kiểm tra xem username đã tồn tại hay chưa
    boolean existsByUsername(String username);

    // Tìm người dùng theo username
    User findByUsername(String username);
}
