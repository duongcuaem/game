package com.game.lyn.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.game.lyn.admin.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Kiểm tra xem username đã tồn tại hay chưa
    boolean existsByUsername(String username);

    // Tìm người dùng theo username
    Admin findByUsername(String username);
}
