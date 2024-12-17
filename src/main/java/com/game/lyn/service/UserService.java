package com.game.lyn.service;


import com.game.lyn.dto.requestDTO.UserRequestDTO;
import com.game.lyn.dto.responseDTO.UserResponseDTO;

import java.util.List;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);
    void deleteUser(Long id);
    Page<UserResponseDTO> getAllUsersWithPagingAndSorting(int page, int size, String sortBy, String sortDirection);

    Page<UserResponseDTO> filterUsersWithPagingAndSorting(
            Map<String, String> filters, int page, int size, String sortBy, String sortDirection);

    UserResponseDTO lockUserAccount(Long id);
    UserResponseDTO unlockUserAccount(Long id);
}