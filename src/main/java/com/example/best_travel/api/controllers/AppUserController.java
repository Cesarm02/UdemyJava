package com.example.best_travel.api.controllers;

import com.example.best_travel.infraestructure.abstrat.ModifyUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path = "user")
@AllArgsConstructor
@Tag(name = "User")
public class AppUserController {

    private final ModifyUserService userService;

    @Operation(summary = "Enabled or disabled user")
    @PatchMapping(path = "enabled-or-disabled")
    public ResponseEntity<Map<String, Boolean>> enabledOrDisabled(@RequestParam String username){
        return ResponseEntity.ok(this.userService.enabled(username));
    }

    @Operation(summary = "Add role")
    @PatchMapping(path = "add-role")
    public ResponseEntity<Map<String, Set<String>>> addRole(@RequestParam String username, @RequestParam String role){
        return ResponseEntity.ok(this.userService.addRole(role, username));
    }

    @Operation(summary = "Delete role")
    @PatchMapping(path = "remove-role")
    public ResponseEntity<Map<String, Set<String>>> RemoveRole(@RequestParam String username, @RequestParam String role){
        return ResponseEntity.ok(this.userService.removeRole(role, username));
    }

}
