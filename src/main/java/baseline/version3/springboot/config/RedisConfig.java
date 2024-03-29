package baseline.version3.springboot.config;

import baseline.version3.springboot.config.properties.RedisCacheProperties;
import baseline.version3.springboot.config.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
//@EnableRedisRepositories
//@EnableRedisHttpSession
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;
    private final RedisCacheProperties redisCacheProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.host(), redisProperties.port());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.password()));
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    @Primary
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(redisCacheProperties.ttl()))
                .prefixCacheNameWith(redisCacheProperties.prefix())       // 캐시 접두사
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer())
                );

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    @Bean
    public HashOperations<?, Object, Object> hashOperations(RedisTemplate<?, ?> redisTemplate){
        return redisTemplate.opsForHash();
    }

    @Bean
    public ValueOperations<?, ?> valueOperations(RedisTemplate<?, ?> redisTemplate){
        return redisTemplate.opsForValue();
    }

    @Bean
    public ListOperations<?, ?> listOperations(RedisTemplate<?, ?> redisTemplate){
        return redisTemplate.opsForList();
    }

    @Bean
    public SetOperations<?, ?> setOperations(RedisTemplate<?, ?> redisTemplate){
        return redisTemplate.opsForSet();
    }

}
