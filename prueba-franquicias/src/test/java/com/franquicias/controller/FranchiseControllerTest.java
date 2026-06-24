package com.franquicias.controller;

import com.franquicias.dto.ProductoConSucursal;
import com.franquicias.model.Branch;
import com.franquicias.model.Franchise;
import com.franquicias.model.Product;
import com.franquicias.service.FranchiseService;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FranchiseControllerTest {

    private final FranchiseService service = mock(FranchiseService.class);
    private final FranchiseController controller = new FranchiseController(service);

    @Test
    void getAllFranchises_ShouldReturnList() {
        when(service.getAllFranchises()).thenReturn(Flux.just(
                createFranchise(1L, "McDonald's"),
                createFranchise(2L, "Burger King")
        ));

        var result = controller.getAllFranchises().collectList().block();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("McDonald's", result.get(0).getName());
    }

    @Test
    void createFranchise_ShouldReturn201() {
        var saved = createFranchise(1L, "KFC");
        when(service.createFranchise(any())).thenReturn(Mono.just(saved));

        var result = controller.createFranchise(new Franchise()).block();

        assertNotNull(result);
        assertEquals("KFC", result.getName());
    }

    @Test
    void updateFranchiseName_ShouldReturn200() {
        var updated = createFranchise(1L, "McDonald's USA");
        when(service.updateFranchiseName(anyLong(), any())).thenReturn(Mono.just(updated));

        var result = controller.updateFranchiseName(1L, Map.of("name", "McDonald's USA")).block();

        assertNotNull(result);
        assertEquals("McDonald's USA", result.getName());
    }

    @Test
    void addBranch_ShouldReturn201() {
        var saved = new Branch();
        saved.setId(1L);
        saved.setName("Sucursal Centro");
        when(service.addBranch(anyLong(), any())).thenReturn(Mono.just(saved));

        var result = controller.addBranch(1L, new Branch()).block();

        assertNotNull(result);
        assertEquals("Sucursal Centro", result.getName());
    }

    @Test
    void updateBranchName_ShouldReturn200() {
        var updated = new Branch();
        updated.setId(1L);
        updated.setName("Sucursal Norte");
        when(service.updateBranchName(anyLong(), any())).thenReturn(Mono.just(updated));

        var result = controller.updateBranchName(1L, Map.of("name", "Sucursal Norte")).block();

        assertNotNull(result);
        assertEquals("Sucursal Norte", result.getName());
    }

    @Test
    void addProduct_ShouldReturn201() {
        var saved = new Product();
        saved.setId(1L);
        saved.setName("Hamburguesa");
        saved.setStock(50);
        when(service.addProduct(anyLong(), any())).thenReturn(Mono.just(saved));

        var result = controller.addProduct(1L, new Product()).block();

        assertNotNull(result);
        assertEquals("Hamburguesa", result.getName());
    }

    @Test
    void updateProductName_ShouldReturn200() {
        var updated = new Product();
        updated.setId(1L);
        updated.setName("Big Mac");
        when(service.updateProductName(anyLong(), any())).thenReturn(Mono.just(updated));

        var result = controller.updateProductName(1L, Map.of("name", "Big Mac")).block();

        assertNotNull(result);
        assertEquals("Big Mac", result.getName());
    }

    @Test
    void updateProductStock_ShouldReturn200() {
        var updated = new Product();
        updated.setId(1L);
        updated.setStock(100);
        when(service.updateProductStock(anyLong(), any())).thenReturn(Mono.just(updated));

        var result = controller.updateProductStock(1L, Map.of("stock", 100)).block();

        assertNotNull(result);
        assertEquals(100, result.getStock());
    }

    @Test
    void getProductsByBranch_ShouldReturnList() {
        when(service.getProductsByBranch(anyLong())).thenReturn(Flux.just(
                createProduct(1L, "Hamburguesa", 50, 1L),
                createProduct(2L, "Papas", 30, 1L)
        ));

        var result = controller.getProductsByBranch(1L).collectList().block();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void deleteProduct_ShouldReturn204() {
        when(service.deleteProduct(anyLong())).thenReturn(Mono.empty());

        var result = controller.deleteProduct(1L).block();

        assertEquals(null, result);
    }

    @Test
    void getTopProducts_ShouldReturnList() {
        when(service.getTopProductsByFranchise(anyLong())).thenReturn(Flux.just(
                new ProductoConSucursal("Hamburguesa", 50, "Sucursal Norte"),
                new ProductoConSucursal("Pizza", 40, "Sucursal Sur")
        ));

        var result = controller.getTopProducts(1L).collectList().block();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Hamburguesa", result.get(0).getProductName());
    }

    private Franchise createFranchise(Long id, String name) {
        var f = new Franchise();
        f.setId(id);
        f.setName(name);
        return f;
    }

    private Product createProduct(Long id, String name, Integer stock, Long branchId) {
        var p = new Product();
        p.setId(id);
        p.setName(name);
        p.setStock(stock);
        p.setBranchId(branchId);
        return p;
    }
}
