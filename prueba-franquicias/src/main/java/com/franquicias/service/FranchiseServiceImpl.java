package com.franquicias.service;

import com.franquicias.dto.ProductoConSucursal;
import com.franquicias.model.Branch;
import com.franquicias.model.Franchise;
import com.franquicias.model.Product;
import com.franquicias.repository.BranchRepository;
import com.franquicias.repository.FranchiseRepository;
import com.franquicias.repository.ProductCacheRepository;
import com.franquicias.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final ProductCacheRepository cacheRepository;

    public FranchiseServiceImpl(FranchiseRepository franchiseRepository,
                                BranchRepository branchRepository,
                                ProductRepository productRepository,
                                ProductCacheRepository cacheRepository) {
        this.franchiseRepository = franchiseRepository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
        this.cacheRepository = cacheRepository;
    }

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        return franchiseRepository.save(franchise);
    }

    @Override
    public Mono<Franchise> updateFranchiseName(Long id, String name) {
        return franchiseRepository.findById(id)
                .flatMap(f -> {
                    f.setName(name);
                    return franchiseRepository.save(f);
                });
    }

    @Override
    public Mono<Branch> addBranch(Long franchiseId, Branch branch) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(f -> {
                    branch.setFranchiseId(franchiseId);
                    return branchRepository.save(branch);
                });
    }

    @Override
    public Mono<Branch> updateBranchName(Long id, String name) {
        return branchRepository.findById(id)
                .flatMap(b -> {
                    b.setName(name);
                    return branchRepository.save(b);
                });
    }

    @Override
    public Mono<Product> addProduct(Long branchId, Product product) {
        return branchRepository.findById(branchId)
                .flatMap(b -> {
                    product.setBranchId(branchId);
                    return productRepository.save(product);
                });
    }

    @Override
    public Mono<Product> updateProductName(Long id, String name) {
        return productRepository.findById(id)
                .flatMap(p -> {
                    p.setName(name);
                    return productRepository.save(p);
                });
    }

    @Override
    public Mono<Product> updateProductStock(Long id, Integer stock) {
        return productRepository.findById(id)
                .flatMap(p -> {
                    p.setStock(stock);
                    return productRepository.save(p);
                });
    }

    @Override
    public Mono<Void> deleteProduct(Long id) {
        return productRepository.deleteById(id);
    }

    @Override
    public Flux<Franchise> getAllFranchises() {
        return franchiseRepository.findAll();
    }

    @Override
    public Flux<Product> getProductsByBranch(Long branchId) {
        return productRepository.findByBranchId(branchId);
    }

    @Override
    public Flux<ProductoConSucursal> getTopProductsByFranchise(Long franchiseId) {
        return cacheRepository.getCachedTopProducts(franchiseId)
                .switchIfEmpty(fetchTopProducts(franchiseId))
                .onErrorResume(e -> fetchTopProducts(franchiseId));
    }

    private Flux<ProductoConSucursal> fetchTopProducts(Long franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId)
                .flatMap(branch ->
                        productRepository.findByBranchId(branch.getId())
                                .sort(Comparator.comparingInt(Product::getStock).reversed())
                                .next()
                                .map(product -> new ProductoConSucursal(
                                        product.getName(),
                                        product.getStock(),
                                        branch.getName()
                                ))
                );
    }
}
