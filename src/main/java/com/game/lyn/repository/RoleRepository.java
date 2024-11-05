package com.game.lyn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.game.lyn.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
     // Phương thức kiểm tra xem Role có tồn tại bằng ID
     boolean existsById(Long id);
}
