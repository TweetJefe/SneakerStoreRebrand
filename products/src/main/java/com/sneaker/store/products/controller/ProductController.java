package com.sneaker.store.products.controller;

import com.sneaker.store.products.dto.ProductCriteria;
import com.sneaker.store.products.dto.ProductDTO;
import com.sneaker.store.products.dto.RRRCProductDTO;
import com.sneaker.store.products.dto.UpdateProductDTO;
import com.sneaker.store.products.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Tag(name="Products", description = "API for work with products")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl service;

    @Operation(
            summary = "Create a product",
            tags = {"Products"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product was created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Product with this name already in system"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Void> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for creating product",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            )
            @RequestBody ProductDTO dto
    ) {
        service.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
                summary = "Update a product",
                tags = {"Products"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<Void> updateProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for updating product",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateProductDTO.class)
                    )
            )
            @RequestBody UpdateProductDTO dto
    ){
        service.updateProduct(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Delete a product",
            tags = {"Products"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product was deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID", example = "13")
            @PathVariable Long id
    ){
        service.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            summary = "Find product by criteria",
            tags = {"Products"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products was returned", content = @Content(
                    array = @ArraySchema(
                            arraySchema = @Schema(description = "Page of products"),
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/criteria/page/{page}")
    public ResponseEntity<?> findProductByCriteria(
            @PathVariable int page,
            @RequestBody ProductCriteria criteria
    ) {
        final int size = 20;
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.findByCriteria(criteria, pageable).join());
    }


    @Operation(
            summary = "Reserve product",
            tags = {"Products"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products was reserved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/reserve")
    public ResponseEntity<Void> reserveProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Special DTO for release-reserve operation",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RRRCProductDTO.class)
                    )
            )
            @RequestBody RRRCProductDTO dto
    ){
        service.reserveProduct(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(
            summary = "Release product",
            tags = {"Products"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products was released"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/release")
    public ResponseEntity<Void> releaseProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Special DTO for release-reserve operation",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RRRCProductDTO.class)
                    )
            )
            @RequestBody RRRCProductDTO dto
    ){
        service.releaseProduct(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/brand/all")
    public ResponseEntity<List<String>> getAllBrands(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllBrands());
    }

    @GetMapping("/sizes/all")
    public ResponseEntity<List<Double>> getAllSizes(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllSizes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable Long id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.getProductById(id));
    }

    @GetMapping("/all/page/{page}")
    public ResponseEntity<Page<ProductDTO>> getAllWithoutCriteria(
            @PathVariable int page
    ){
        final int size = 20;
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllWithoutCriteria(pageable));
    }

    @GetMapping("/recommend/{id}/price")
    public ResponseEntity<List<ProductDTO>> findRecommendByPrice(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable Long id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.findRecommendByPrice(id));
    }

    @GetMapping("/recommend/{id}/brand")
    public ResponseEntity<List<ProductDTO>> findRecommendByBrand(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable Long id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.findRecommendByBrand(id));
    }

}