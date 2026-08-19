package com.quoc.identity.service.service;

import com.quoc.identity.service.dto.request.UserCreationRequest;
import com.quoc.identity.service.dto.request.UserUpdateRequest;
import com.quoc.identity.service.dto.response.UserResponse;
import com.quoc.identity.service.entity.User;
import com.quoc.identity.service.exception.AppException;
import com.quoc.identity.service.exception.ErrorCode;
import com.quoc.identity.service.mapper.UserMapper;
import com.quoc.identity.service.repository.RoleRepository;
import com.quoc.identity.service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class UserService {

    UserMapper userMapper;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    // =========================================================
    // CREATE USER
    // =========================================================

    public User createUser(UserCreationRequest request) {

        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Tạo User mới
        User user = userMapper.toUser(request);

        // Mã hóa password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Kiểm tra roles có được gửi lên không
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        // Tìm role trong database
        var roles = roleRepository.findAllById(request.getRoles());

        // Kiểm tra tất cả role có tồn tại không
        if (roles.size() != request.getRoles().size()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        // Gán role cho user
        user.setRoles(new HashSet<>(roles));

        // Lưu user
        return userRepository.save(user);
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    // =========================================================
    // UPDATE USER
    // =========================================================

    public UserResponse updateUser(
            UUID userId,
            UserUpdateRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND)
                );

        userMapper.updateUser(user, request);

        if (request.getPassword() != null) {
            user.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
        }

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {

            var roles = roleRepository.findAllById(request.getRoles());

            if (roles.size() != request.getRoles().size()) {
                throw new AppException(ErrorCode.ROLE_NOT_FOUND);
            }

            user.setRoles(new HashSet<>(roles));
        }

        return userMapper.toUserResponse(
                userRepository.save(user)
        );
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

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");

        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }


    // =========================================================
    // GET USER BY ID
    // =========================================================

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(UUID id) {
        log.info("In method get user by Id");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)));

    }



}