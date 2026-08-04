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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ApiResponse<List<User>> getUsers() {

        ApiResponse<List<User>> response = new ApiResponse<>();

        response.setResult(
                userService.getUsers()
        );

        return response;
    }


    // GET USER BY ID
    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") UUID userId){
        return userService.getUser(userId);

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

    //Roles
    @PostMapping("/{userId}/roles/{roleId}")
    public ApiResponse<User> assignRole(
            @PathVariable UUID userId,
            @PathVariable UUID roleId
    ) {

        ApiResponse<User> response = new ApiResponse<>();

        response.setResult(
                userService.assignRole(userId, roleId)
        );

        return response;
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ApiResponse<User> removeRole(
            @PathVariable UUID userId,
            @PathVariable UUID roleId
    ) {

        ApiResponse<User> response = new ApiResponse<>();

        response.setResult(
                userService.removeRole(userId, roleId)
        );

        return response;
    }
}