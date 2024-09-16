package com.game.lyn.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.game.lyn.user.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
