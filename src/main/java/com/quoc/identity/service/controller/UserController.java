package com.quoc.identity.service.controller;

import com.quoc.identity.service.dto.request.ApiResponse;
import com.quoc.identity.service.dto.request.UserCreationRequest;
import com.quoc.identity.service.dto.request.UserUpdateRequest;
import com.quoc.identity.service.dto.response.UserResponse;
import com.quoc.identity.service.entity.User;
import com.quoc.identity.service.service.UserService;

import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {

    UserService userService;

    // CREATE USER
    @PostMapping
    public ApiResponse<User> createUser(
            @RequestBody @Valid UserCreationRequest request
    ) {

        ApiResponse<User> response = new ApiResponse<>();

        response.setResult(
                userService.createUser(request)
        );

        return response;
    }


    // GET ALL USERS
    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        ApiResponse<List<UserResponse>> response = new ApiResponse<>();

        response.setResult(
                userService.getUsers()
        );

        return response;
    }


    // GET USER BY ID
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") UUID userId){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();

    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();

    }


    // UPDATE USER
    @PutMapping("/{userId}")
    UserResponse updateUser(
            @PathVariable UUID userId,
            @RequestBody  UserUpdateRequest request){
        return userService.updateUser(userId, request);

    }


    // DELETE USER
    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(
            @PathVariable UUID userId
    ) {

        userService.deleteUser(userId);

        ApiResponse<String> response = new ApiResponse<>();

        response.setMessage("User has been deleted");

        return response;
    }


}