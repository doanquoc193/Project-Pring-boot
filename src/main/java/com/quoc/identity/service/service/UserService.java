package com.quoc.identity.service.service;

import com.quoc.identity.service.dto.request.UserCreationRequest;
import com.quoc.identity.service.dto.request.UserUpdateRequest;
import com.quoc.identity.service.entity.User;
import com.quoc.identity.service.exception.AppException;
import com.quoc.identity.service.exception.ErrorCode;
import com.quoc.identity.service.respository.UserRepository;
import com.quoc.identity.service.entity.Role;
import com.quoc.identity.service.respository.RoleRepository;

import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


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
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setDob(request.getDob());

        // Lưu vào database
        return userRepository.save(user);
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    public User updateUser(
            UUID userId,
            UserUpdateRequest request
    ) {

        // Tìm User
        // Nếu không tìm thấy -> AppException
        User user = getUser(userId);

        // Cập nhật thông tin
        user.setPassword(request.getPassword());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setDob(request.getDob());

        // Lưu lại database
        return userRepository.save(user);
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

    public User getUser(UUID id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new AppException(
                                ErrorCode.USER_NOT_FOUND
                        )
                );
    }

    @Autowired
    private RoleRepository roleRepository;

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