package com.franquicias.service;

import com.franquicias.dto.ProductoConSucursal;
import com.franquicias.model.Branch;
import com.franquicias.model.Franchise;
import com.franquicias.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseService {

    Mono<Franchise> createFranchise(Franchise franchise);

    Mono<Franchise> updateFranchiseName(Long id, String name);

    Mono<Branch> addBranch(Long franchiseId, Branch branch);

    Mono<Branch> updateBranchName(Long id, String name);

    Mono<Product> addProduct(Long branchId, Product product);

    Mono<Product> updateProductName(Long id, String name);

    Mono<Product> updateProductStock(Long id, Integer stock);

    Mono<Void> deleteProduct(Long id);

    Flux<ProductoConSucursal> getTopProductsByFranchise(Long franchiseId);

    Flux<Product> getProductsByBranch(Long branchId);
}
