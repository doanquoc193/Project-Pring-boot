package com.quoc.identity.service.mapper;

import com.quoc.identity.service.dto.request.UserCreationRequest;
import com.quoc.identity.service.dto.request.UserUpdateRequest;
import com.quoc.identity.service.dto.response.UserResponse;
import com.quoc.identity.service.entity.Role;
import com.quoc.identity.service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = RoleMapper.class
)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(
            @MappingTarget User user,
            UserUpdateRequest request
    );


}