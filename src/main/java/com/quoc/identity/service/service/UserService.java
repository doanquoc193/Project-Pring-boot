package com.quoc.identity.service.service;

import com.quoc.identity.service.dto.request.UserCreationRequest;
import com.quoc.identity.service.dto.request.UserUpdateRequest;
import com.quoc.identity.service.dto.response.UserResponse;
import com.quoc.identity.service.entity.User;
import com.quoc.identity.service.exception.AppException;
import com.quoc.identity.service.exception.ErrorCode;
import com.quoc.identity.service.mapper.UserMapper;
import com.quoc.identity.service.respository.UserRepository;
import com.quoc.identity.service.entity.Role;
import com.quoc.identity.service.respository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserService {

    UserMapper userMapper;
    UserRepository userRepository;

    // =========================================================
    // CREATE USER
    // =========================================================

    public User createUser(UserCreationRequest request) {

        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(request.getUsername())) {

            throw new AppException(
                    ErrorCode.USER_EXISTED
            );
        }

        // Tạo User mới
        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Lưu vào database
        return userRepository.save(user);
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    public UserResponse updateUser(
            UUID userId,
            UserUpdateRequest request
    ) {

        // Tìm User
        // Nếu không tìm thấy -> AppException
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        userMapper.updateUser(user, request);

        // Lưu lại database
        return userMapper.toUserResponse(userRepository.save(user));
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    public void deleteUser(UUID userId) {

        // Tìm User
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new AppException(
                                ErrorCode.USER_NOT_FOUND
                        )
                );

        // Xóa User
        userRepository.delete(user);
    }


    // =========================================================
    // GET ALL USERS
    // =========================================================

    public List<User> getUsers() {

        return userRepository.findAll();
    }


    // =========================================================
    // GET USER BY ID
    // =========================================================

    public UserResponse getUser(UUID id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found")));

    }


    RoleRepository roleRepository;

    public User assignRole(UUID userId, UUID roleId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND)
                );

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.ROLE_NOT_FOUND)
                );

        user.getRoles().add(role);

        return userRepository.save(user);
    }

    public User removeRole(UUID userId, UUID roleId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND)
                );

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.ROLE_NOT_FOUND)
                );

        user.getRoles().remove(role);

        return userRepository.save(user);
    }
}