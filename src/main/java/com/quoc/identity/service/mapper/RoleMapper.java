package com.quoc.identity.service.mapper;

import com.quoc.identity.service.dto.request.RoleRequest;
import com.quoc.identity.service.dto.request.UserUpdateRequest;
import com.quoc.identity.service.dto.response.RoleResponse;
import com.quoc.identity.service.entity.Permission;
import com.quoc.identity.service.entity.Role;
import com.quoc.identity.service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    @Mapping(target = "permissions", source = "permissions")
    RoleResponse toRoleResponse(Role role);


    default Set<String> map(Set<Permission> permissions) {
        if (permissions == null) {
            return null;
        }

        return permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
}