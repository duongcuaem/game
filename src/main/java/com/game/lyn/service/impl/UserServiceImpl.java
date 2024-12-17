package com.game.lyn.service.impl;

import com.game.lyn.exception.CustomException;
import com.game.lyn.service.AuthService;
import com.game.lyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.game.lyn.entity.User;
import com.game.lyn.entity.Role;
import com.game.lyn.repository.UserRepository;

import com.game.lyn.dto.requestDTO.UserRequestDTO;
import com.game.lyn.dto.responseDTO.UserResponseDTO;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.*;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Tạo logger
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

//    public Optional<User> getUserById(Long id) {
//        return userRepository.findById(id);
//    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found", "The user with id " + id + " does not exist", HttpStatus.NOT_FOUND));
        return convertToResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new CustomException("No users found", "There are no users in the database", HttpStatus.NOT_FOUND);
        }
        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            throw new CustomException("User already exists", "A user with username " + userRequestDTO.getUsername() + " already exists", HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .password(passwordEncoder.encode(userRequestDTO.getPassword())) // Mã hóa mật khẩu
                .banPass(userRequestDTO.getBanPass())
                .banLogin(userRequestDTO.getBanLogin())
                .token(userRequestDTO.getToken())
                .lastDate(userRequestDTO.getLastDate())
                .lastLogin(userRequestDTO.getLastLogin())
                .regDate(userRequestDTO.getRegDate())
                .fail(userRequestDTO.getFail())
                .lock(userRequestDTO.getLock())
                .build();
        user = userRepository.save(user);
        return convertToResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found", "The user with id " + id + " does not exist", HttpStatus.NOT_FOUND));

        if (!user.getUsername().equals(userRequestDTO.getUsername()) &&
                userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            throw new CustomException("Username already in use", "The username " + userRequestDTO.getUsername() + " is already taken", HttpStatus.BAD_REQUEST);
        }

        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword())); // Mã hóa nếu cần
        user.setBanPass(userRequestDTO.getBanPass());
        user.setBanLogin(userRequestDTO.getBanLogin());
        user.setToken(userRequestDTO.getToken());
        user.setLastDate(userRequestDTO.getLastDate());
        user.setLastLogin(userRequestDTO.getLastLogin());
        user.setRegDate(userRequestDTO.getRegDate());
        user.setFail(userRequestDTO.getFail());
        user.setLock(userRequestDTO.getLock());
        user = userRepository.save(user);
        return convertToResponseDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomException("User not found", "The user with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO lockUserAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found", "The user with id " + id + " does not exist", HttpStatus.NOT_FOUND));
        user.setBanLogin(true);
        user = userRepository.save(user);
        return convertToResponseDTO(user);
    }

    @Override
    public UserResponseDTO unlockUserAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found", "The user with id " + id + " does not exist", HttpStatus.NOT_FOUND));
        user.setBanLogin(false);
        user = userRepository.save(user);
        return convertToResponseDTO(user);
    }


    @Override
    public Page<UserResponseDTO> getAllUsersWithPagingAndSorting(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> usersPage = userRepository.findAll(pageable);

        return usersPage.map(this::convertToResponseDTO);
    }

    @Override
    public Page<UserResponseDTO> filterUsersWithPagingAndSorting(
            Map<String, String> filters, int page, int size, String sortBy, String sortDirection) {
        try {
            // Tạo đối tượng Sort
            Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);

            // Khởi tạo Specification
            Specification<User> specification = Specification.where(null);

            // Kiểm tra và áp dụng filter
            for (Map.Entry<String, String> filter : filters.entrySet()) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(filter.getKey()), "%" + filter.getValue() + "%"));
            }

            // Thực hiện truy vấn
            Page<User> filteredUsers = userRepository.findAll(specification, pageable);
            return filteredUsers.map(this::convertToResponseDTO);

        } catch (IllegalArgumentException e) {
            // Ghi log lỗi
            logger.error("Khóa lọc không hợp lệ được cung cấp", e.getMessage());
            throw new CustomException("Khóa lọc không hợp lệ được cung cấp", e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Xử lý lỗi chung, tránh crash ứng dụng
            throw new CustomException("An unexpected error occurred", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()))
                .banPass(user.getBanPass())
                .banLogin(user.getBanLogin())
                .lastDate(user.getLastDate())
                .lastLogin(user.getLastLogin())
                .regDate(user.getRegDate())
                .lock(user.getLock())
                .build();
    }
}
