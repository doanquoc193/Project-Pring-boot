package com.quoc.identity.service.controller;

import com.quoc.identity.service.dto.request.ApiResponse;
import com.quoc.identity.service.entity.Role;
import com.quoc.identity.service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // CREATE ROLE
    @PostMapping
    public ApiResponse<Role> createRole(
            @RequestBody Role request
    ) {

        ApiResponse<Role> response = new ApiResponse<>();

        response.setResult(
                roleService.createRole(request)
        );

        return response;
    }

    // GET ALL ROLES
    @GetMapping
    public ApiResponse<List<Role>> getRoles() {

        ApiResponse<List<Role>> response = new ApiResponse<>();

        response.setResult(
                roleService.getRoles()
        );

        return response;
    }

    // GET ROLE BY ID
    @GetMapping("/{roleId}")
    public ApiResponse<Role> getRole(
            @PathVariable UUID roleId
    ) {

        ApiResponse<Role> response = new ApiResponse<>();

        response.setResult(
                roleService.getRole(roleId)
        );

        return response;
    }

    // UPDATE ROLE
    @PutMapping("/{roleId}")
    public ApiResponse<Role> updateRole(
            @PathVariable UUID roleId,
            @RequestBody Role request
    ) {

        ApiResponse<Role> response = new ApiResponse<>();

        response.setResult(
                roleService.updateRole(roleId, request)
        );

        return response;
    }

    // DELETE ROLE
    @DeleteMapping("/{roleId}")
    public ApiResponse<String> deleteRole(
            @PathVariable UUID roleId
    ) {

        roleService.deleteRole(roleId);

        ApiResponse<String> response = new ApiResponse<>();

        response.setMessage("Role has been deleted");

        return response;
    }


}