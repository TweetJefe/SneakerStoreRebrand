package com.sneaker.store.products.controller;

import com.sneaker.store.products.dto.ProductCriteria;
import com.sneaker.store.products.dto.ProductDTO;
import com.sneaker.store.products.dto.RRRCProductDTO;
import com.sneaker.store.products.dto.UpdateProductDTO;
import com.sneaker.store.products.enums.Category;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @GetMapping("/criteria")
    public CompletableFuture<ResponseEntity<Page<ProductDTO>>> findProductByCriteria(
            @Parameter(description = "Criterias for searching")
            @ModelAttribute ProductCriteria criteria,
            Pageable pageable
    ){
        return service.findByCriteria(criteria, pageable).thenApply(product -> ResponseEntity.status(HttpStatus.OK).body(product));
    }

    @Operation(
            summary = "Find product by brand",
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
    @GetMapping("/brand")
    public CompletableFuture<ResponseEntity<Page<ProductDTO>>> findProductByBrand(
            @Parameter(description = "Product brand", example = "Nike")
            @RequestParam String brand,
            Pageable pageable
    ){
        return service.getProductByBrand(brand, pageable).thenApply(product -> ResponseEntity.status(HttpStatus.OK).body(product));
    }

    @Operation(
            summary = "Find product by price",
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
    @GetMapping("/price")
    public CompletableFuture<ResponseEntity<Page<ProductDTO>>> findProductByPrice(
            @Parameter(description = "Price of product", example = "74.95")
            @RequestParam double price,
            Pageable pageable
    ){
        return service.getProductByPrice(price, pageable).thenApply(product -> ResponseEntity.status(HttpStatus.OK).body(product));
    }


    @Operation(
            summary = "Find product by category",
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
    @GetMapping("/category")
    public CompletableFuture<ResponseEntity<Page<ProductDTO>>> findProductByCategory(
            @Parameter(description = "Product category", example = "Football")
            @RequestParam Category category,
            Pageable pageable
    ){
        return service.getProductByCategory(category, pageable).thenApply(product -> ResponseEntity.status(HttpStatus.OK).body(product));
    }

    @Operation(
            summary = "Find product by name",
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
    @GetMapping("/name")
    public CompletableFuture<ResponseEntity<Page<ProductDTO>>> findProductByName(
            @Parameter(description = "Product Name", example = "...")
            @RequestParam String name,
            Pageable pageable
    ){
        return service.findProductByName(name, pageable).thenApply(product -> ResponseEntity.status(HttpStatus.OK).body(product));
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
}