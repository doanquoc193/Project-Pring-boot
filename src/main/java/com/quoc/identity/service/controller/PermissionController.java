package com.quoc.identity.service.controller;

import com.quoc.identity.service.dto.request.ApiResponse;
import com.quoc.identity.service.dto.request.PermissionRequest;
import com.quoc.identity.service.dto.response.PermissionResponse;
import com.quoc.identity.service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PermissionController {
    PermissionService permissionService;
    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }
    @GetMapping

    ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permissions}")
    ApiResponse<Void> delete(@PathVariable String permissions){
        permissionService.delete(permissions);
        return ApiResponse.<Void>builder().build();
    }

}
