package com.task.mgmt.web;

import com.task.mgmt.dto.RoleDTO;
import com.task.mgmt.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Roles", description = "roles related operations")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/roles")
public class RoleController {

  private final RoleService roleService;

  @Operation(summary = "List All Roles")
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/all")
  public ResponseEntity<List<RoleDTO>> list() {
    final var response = roleService.list();
    return ResponseEntity.ok(response);
  }
}
