package com.game.lyn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.game.lyn.entity.User;
import com.game.lyn.repository.UserRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setBanLogin(userDetails.getBanLogin());
        user.setToken(userDetails.getToken());
        user.setLastDate(userDetails.getLastDate());
        user.setLastLogin(userDetails.getLastLogin());
        user.setRegDate(userDetails.getRegDate());
        user.setFail(userDetails.getFail());
        user.setLock(userDetails.getLock());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);  // Xóa User và các UserInfo liên quan
    }

    public User lockUserAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBanLogin(true);  // Đặt banLogin thành true để khóa tài khoản
        return userRepository.save(user);  // Lưu thay đổi
    }

    public User unlockUserAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBanLogin(false);  // Đặt banLogin thành false để mở khóa tài khoản
        return userRepository.save(user);  // Lưu thay đổi
    }
}
