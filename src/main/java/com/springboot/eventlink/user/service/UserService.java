package com.springboot.eventlink.user.service;

import com.springboot.eventlink.user.dto.UserDTO;
import com.springboot.eventlink.user.dto.UserUpdateRequest;
import com.springboot.eventlink.user.entity.Users;
import com.springboot.eventlink.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void updateUser(String username, UserUpdateRequest request) {
        Users user = userRepository.findByUsername(username);

        user.setGender(request.getGender());
        user.setCountry(request.getCountry());
        userRepository.save(user);
    }

    public UserDTO getUserInfo(String username) {
        Users user = userRepository.findByUsername(username);

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCountry(user.getCountry());
        dto.setGender(user.getGender());
        dto.setRole(user.getRole());
        dto.setSocialType(user.getSocialType());
        return dto;
    }
}
