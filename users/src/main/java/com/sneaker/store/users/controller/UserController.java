package com.sneaker.store.users.controller;

import com.sneaker.store.users.dto.UserCreateDTO;
import com.sneaker.store.users.dto.UserProfileDTO;
import com.sneaker.store.users.dto.UserUpdateDTO;
import com.sneaker.store.users.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "API for work with users")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl service;

    @Operation(
            summary = "Register a user",
            tags = {"Users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User was registered"),
            @ApiResponse(responseCode = "401", description = "Unathorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Email was already registered"),
            @ApiResponse(responseCode = "409", description = "Phone number was already registered"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for register user",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateDTO.class)
                    )
            )
            @RequestBody UserCreateDTO dto
    ) {
        service.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(
            summary = "Update a user",
            tags = {"Users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was updated"),
            @ApiResponse(responseCode = "400", description = "User cannot set same email"),
            @ApiResponse(responseCode = "400", description = "User cannot set same phone"),
            @ApiResponse(responseCode = "401", description = "Unathorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Email was already registered"),
            @ApiResponse(responseCode = "409", description = "Phone number was already registered"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}/update")
    public ResponseEntity<Void> updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for update user",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdateDTO.class)
                    )
            )
            @RequestBody UserUpdateDTO dto,
            @Parameter(description = "User ID", example = "12")
            @PathVariable Long id
    ) {
        service.updateUser(dto, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Update a user",
            tags = {"Users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User was deleted"),
            @ApiResponse(responseCode = "401", description = "Unathorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "12")
            @PathVariable Long id
    ) {
        service.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Get user profile",
            tags = {"Users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile was returned"),
            @ApiResponse(responseCode = "401", description = "Unathorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(
            @Parameter(description = "User ID", example = "12")
            @PathVariable Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getUser(id));
    }
}
