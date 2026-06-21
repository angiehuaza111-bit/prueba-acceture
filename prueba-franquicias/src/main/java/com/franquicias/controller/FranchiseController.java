package com.franquicias.controller;

import com.franquicias.dto.ProductoConSucursal;
import com.franquicias.model.Branch;
import com.franquicias.model.Franchise;
import com.franquicias.model.Product;
import com.franquicias.service.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Franquicias", description = "API de gestión de franquicias, sucursales y productos")
public class FranchiseController {

    private final FranchiseService service;

    public FranchiseController(FranchiseService service) {
        this.service = service;
    }

    @GetMapping("/franchises")
    @Operation(summary = "Obtener todas las franquicias")
    public Flux<Franchise> getAllFranchises() {
        return service.getAllFranchises();
    }

    @PostMapping("/franchises")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear una nueva franquicia")
    public Mono<Franchise> createFranchise(@RequestBody Franchise franchise) {
        return service.createFranchise(franchise);
    }

    @PutMapping("/franchises/{id}/name")
    @Operation(summary = "Actualizar el nombre de una franquicia")
    public Mono<Franchise> updateFranchiseName(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return service.updateFranchiseName(id, body.get("name"));
    }

    @PostMapping("/franchises/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agregar una sucursal a una franquicia")
    public Mono<Branch> addBranch(@PathVariable Long franchiseId, @RequestBody Branch branch) {
        return service.addBranch(franchiseId, branch);
    }

    @PutMapping("/branches/{id}/name")
    @Operation(summary = "Actualizar el nombre de una sucursal")
    public Mono<Branch> updateBranchName(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return service.updateBranchName(id, body.get("name"));
    }

    @PostMapping("/branches/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agregar un producto a una sucursal")
    public Mono<Product> addProduct(@PathVariable Long branchId, @RequestBody Product product) {
        return service.addProduct(branchId, product);
    }

    @PutMapping("/products/{id}/name")
    @Operation(summary = "Actualizar el nombre de un producto")
    public Mono<Product> updateProductName(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return service.updateProductName(id, body.get("name"));
    }

    @PatchMapping("/products/{id}/stock")
    @Operation(summary = "Modificar el stock de un producto")
    public Mono<Product> updateProductStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        return service.updateProductStock(id, body.get("stock"));
    }

    @GetMapping("/branches/{branchId}/products")
    @Operation(summary = "Listar productos de una sucursal")
    public Flux<Product> getProductsByBranch(@PathVariable Long branchId) {
        return service.getProductsByBranch(branchId);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un producto")
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        return service.deleteProduct(id);
    }

    @GetMapping("/franchises/{franchiseId}/top-products")
    @Operation(summary = "Obtener el producto con más stock por sucursal de una franquicia")
    public Flux<ProductoConSucursal> getTopProducts(@PathVariable Long franchiseId) {
        return service.getTopProductsByFranchise(franchiseId);
    }
}
