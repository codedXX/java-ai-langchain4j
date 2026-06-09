package com.dyx.config;

import com.dyx.component.MongoChatMemoryStore;
import dev.langchain4j.community.store.embedding.redis.RedisEmbeddingStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class XiaozhiAgentConfig {
    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;

    /**
     * 向量模型：由 dashscope starter 根据 application.yaml 的 embedding-model 配置自动装配
     * （即 text-embedding-v4）。ingest 和检索都用它，保证向量一致。
     */
    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * Redis 向量存储：由 RedisVectorConfig 定义的 Bean 注入。
     */
    @Autowired
    private RedisEmbeddingStore redisEmbeddingStore;

    @Bean
    ChatMemoryProvider chatMemoryProviderXiaozhi() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(mongoChatMemoryStore)
                .build();
    }

    @Bean
    ContentRetriever contentRetrieverXiaozhi() {
        // 仅当 Redis 向量库为空时才加载并灌入知识库，避免每次启动重复 ingest
        if (isEmbeddingStoreEmpty()) {
            // 读取知识库文档
            Document document1 = FileSystemDocumentLoader.loadDocument("D:\\myProjects\\largeModelProject\\java-ai-langchain4j\\src\\main\\resources\\医院信息.md");
            Document document2 = FileSystemDocumentLoader.loadDocument("D:\\myProjects\\largeModelProject\\java-ai-langchain4j\\src\\main\\resources\\科室信息.md");
            Document document3 = FileSystemDocumentLoader.loadDocument("D:\\myProjects\\largeModelProject\\java-ai-langchain4j\\src\\main\\resources\\神经内科.md");
            List<Document> documents = Arrays.asList(document1, document2, document3);

            // 切割 + 向量化 + 存入 Redis（显式指定 embeddingModel）
            EmbeddingStoreIngestor.builder()
                    .embeddingStore(redisEmbeddingStore)
                    .embeddingModel(embeddingModel)
                    .build()
                    .ingest(documents);
        }

        // 构建检索器，绑定同一个向量库和向量模型
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(redisEmbeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(5)      // 最多返回 5 个相关片段
                .minScore(0.6)      // 相似度低于 0.6 的过滤掉
                .build();
    }

    /**
     * 判断 Redis 向量库是否为空：用一个探针向量检索一条，查不到即视为空库。
     */
    private boolean isEmbeddingStoreEmpty() {
        Embedding probe = embeddingModel.embed("ping").content();
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(probe)
                .maxResults(1)
                .build();
        return redisEmbeddingStore.search(request).matches().isEmpty();
    }
}
