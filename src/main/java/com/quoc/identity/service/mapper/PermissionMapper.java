package com.quoc.identity.service.mapper;

import com.quoc.identity.service.dto.request.PermissionRequest;
import com.quoc.identity.service.dto.request.UserCreationRequest;
import com.quoc.identity.service.dto.request.UserUpdateRequest;
import com.quoc.identity.service.dto.response.PermissionResponse;
import com.quoc.identity.service.dto.response.UserResponse;
import com.quoc.identity.service.entity.Permission;
import com.quoc.identity.service.entity.Role;
import com.quoc.identity.service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.security.Permissions;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);


    default Set<String> map(Set<Role> roles) {
        if (roles == null) {
            return null;
        }

        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}