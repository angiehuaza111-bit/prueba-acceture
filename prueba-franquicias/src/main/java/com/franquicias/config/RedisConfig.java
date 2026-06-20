package com.franquicias.config;

import com.franquicias.dto.ProductoConSucursal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, ProductoConSucursal> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory) {

        var valueSerializer = new Jackson2JsonRedisSerializer<>(ProductoConSucursal.class);
        var context = RedisSerializationContext
                .<String, ProductoConSucursal>newSerializationContext()
                .key(StringRedisSerializer.UTF_8)
                .value(valueSerializer)
                .hashKey(StringRedisSerializer.UTF_8)
                .hashValue(valueSerializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
