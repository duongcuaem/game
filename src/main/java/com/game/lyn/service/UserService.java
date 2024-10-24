package com.game.lyn.service;

import org.springframework.stereotype.Service;

import com.game.lyn.entity.User;
import com.game.lyn.exception.CustomException;
import com.game.lyn.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Cacheable(value = "users", key = "'user_' + #id")  // Cache kết quả với key là id
    public User getUserById(Long id) {
        if (id == null) {
            throw new CustomException("", "Tài khoản không tồn tại : ID", HttpStatus.BAD_REQUEST);
        }
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @CacheEvict(value = "users", key = "#id")  // Xóa cache khi xóa người dùng
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        System.out.println("Deleting user from database...");
    }

    @CachePut(value = "users", key = "#user.id")  // Cập nhật cache khi cập nhật người dùng
    public User updateUser(User user) {
        System.out.println("Updating user in database...");
        // Logic cập nhật người dùng trong database
        return user;
    }
}