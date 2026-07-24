package com.quoc.identity.service.service;

import com.quoc.identity.service.entity.Role;
import com.quoc.identity.service.exception.AppException;
import com.quoc.identity.service.exception.ErrorCode;
import com.quoc.identity.service.respository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // CREATE ROLE
    public Role createRole(Role role) {

        if (roleRepository.existsByName(role.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        return roleRepository.save(role);
    }

    // GET ALL ROLES
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    // GET ROLE BY ID
    public Role getRole(UUID roleId) {

        return roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.ROLE_NOT_FOUND)
                );
    }

    // UPDATE ROLE
    public Role updateRole(UUID roleId, Role request) {

        Role role = getRole(roleId);

        role.setName(request.getName());
        role.setDescription(request.getDescription());

        return roleRepository.save(role);
    }

    // DELETE ROLE
    public void deleteRole(UUID roleId) {

        Role role = getRole(roleId);

        roleRepository.delete(role);
    }
}