package com.franquicias.repository;

import com.franquicias.dto.ProductoConSucursal;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
public class ProductCacheRepository {

    private final ReactiveRedisTemplate<String, ProductoConSucursal> redisTemplate;
    private static final String CACHE_PREFIX = "top-products:";
    private static final Duration TTL = Duration.ofMinutes(10);

    public ProductCacheRepository(ReactiveRedisTemplate<String, ProductoConSucursal> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<Void> cacheTopProducts(Long franchiseId, Flux<ProductoConSucursal> products) {
        String key = CACHE_PREFIX + franchiseId;
        return redisTemplate.opsForList()
                .delete(key)
                .thenMany(
                        products.flatMap(p ->
                                redisTemplate.opsForList()
                                        .rightPush(key, p)
                        )
                )
                .then(redisTemplate.expire(key, TTL))
                .then();
    }

    public Flux<ProductoConSucursal> getCachedTopProducts(Long franchiseId) {
        String key = CACHE_PREFIX + franchiseId;
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
