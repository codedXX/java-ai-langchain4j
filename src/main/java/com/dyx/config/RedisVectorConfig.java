package com.dyx.config;

import dev.langchain4j.community.store.embedding.redis.RedisEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 向量数据库配置。
 * 把 RedisEmbeddingStore 定义成独立的 Spring Bean，参数从 application.yaml 的 rag.redis.* 读取，
 * 其它地方通过 @Autowired 注入使用。
 */
@Configuration
public class RedisVectorConfig {

    @Value("${rag.redis.host}")
    private String host;

    @Value("${rag.redis.port}")
    private Integer port;

    @Value("${rag.redis.index-name}")
    private String indexName;

    @Value("${rag.redis.prefix}")
    private String prefix;

    @Value("${rag.redis.dimension}")
    private Integer dimension;

    /**
     * 构建 Redis 向量存储对象（基于 RediSearch）。
     * 构造时会自动在 Redis 里创建对应的向量索引。
     */
    @Bean
    public RedisEmbeddingStore redisEmbeddingStore() {
        return RedisEmbeddingStore.builder()
                .host(host)
                .port(port)
                .indexName(indexName)
                .prefix(prefix)
                .dimension(dimension)   // 维度需与 embedding 模型输出维度一致
                .build();
    }
}
