package com.sneaker.store.users.controller;

import com.sneaker.store.users.dto.*;
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

import java.security.Principal;

@Tag(name = "Users", description = "API for work with users")
@RestController
@RequestMapping("/api/users")
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
            summary = "Update an email",
            tags = {"Users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email was updated"),
            @ApiResponse(responseCode = "400", description = "User cannot set same email"),
            @ApiResponse(responseCode = "401", description = "Unathorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Email was already registered"),
            @ApiResponse(responseCode = "409", description = "Phone number was already registered"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update/email")
    public ResponseEntity<Void> updateEmail(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for update users email",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateEmailDTO.class)
                    )
            )
            @RequestBody UpdateEmailDTO dto,
            Principal principal
    ) {
        String email = principal.getName();
        service.updateEmail(dto, email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Update an address",
            tags = {"Users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address was updated"),
            @ApiResponse(responseCode = "400", description = "User cannot set same email"),
            @ApiResponse(responseCode = "401", description = "Unathorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Email was already registered"),
            @ApiResponse(responseCode = "409", description = "Phone number was already registered"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update/address")
    public ResponseEntity<Void> updateAddress(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for update users address",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateAddressDTO.class)
                    )
            )
            @RequestBody UpdateAddressDTO dto,
            Principal principal
    ) {
        String email = principal.getName();
        service.updateAddress(dto, email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Update an address",
            tags = {"Users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email was updated"),
            @ApiResponse(responseCode = "400", description = "User cannot set same email"),
            @ApiResponse(responseCode = "401", description = "Unathorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Email was already registered"),
            @ApiResponse(responseCode = "409", description = "Phone number was already registered"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update/password")
    public ResponseEntity<Void> updatePassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for update users password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdatePasswordDTO.class)
                    )
            )
            @RequestBody UpdatePasswordDTO dto,
            Principal principal
    ) {
        String email = principal.getName();
        service.updatePassword(dto, email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Delete a user",
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
    @GetMapping("/profile/me")
    public ResponseEntity<UserProfileDTO> getMyProfile(
            Principal principal
    ) {
        String email = principal.getName();
        return ResponseEntity.ok(service.getUserProfileByEmail(email));
    }

    @PatchMapping("/{id}/favorites/add")
    public ResponseEntity<Void> addToFavorites(
            @PathVariable Long id,
            Principal principal
    ){
        String email = principal.getName();
        service.addToFavorites(id, email);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{id}/favorites/delete")
    public ResponseEntity<Void> removeFromFavorites(
            @PathVariable Long id,
            Principal principal
    ){
        String email = principal.getName();
        service.removeFromFavorites(id, email);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }
}
