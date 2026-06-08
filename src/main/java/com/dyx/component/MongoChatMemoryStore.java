package com.dyx.component;

import com.dyx.pojo.ChatMessages;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * 自定义聊天记忆持久化存储：将 LangChain4j 的聊天记录保存到 MongoDB。
 * <p>
 * 实现 LangChain4j 的 {@link ChatMemoryStore} 接口，替换默认的内存存储，
 * 使聊天记录在应用重启后依然不丢失。框架会在每次对话时自动回调下面这三个方法。
 */
@Component // 注册为 Spring Bean，便于在配置类中注入并设置给 ChatMemoryProvider
public class MongoChatMemoryStore implements ChatMemoryStore {

    /**
     * Spring Data 提供的 MongoDB 操作模板，用于对 MongoDB 增删改查。
     */
    @Autowired
    private MongoTemplate mongoTemplate;// Spring 操作 MongoDB 的工具

    /**
     * 读取指定会话的历史消息。
     * 框架在每次对话前调用，用于把历史上下文拼进本次请求。
     *
     * @param memoryId 会话唯一标识
     * @return 该会话的历史消息列表；不存在则返回空列表（表示新会话）
     */
    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        // 构造查询条件：memoryId 字段 = 传入的会话 id
        Criteria criteria = Criteria.where("memoryId").is(memoryId);
        Query query = new Query(criteria);
        // 查询该会话对应的文档
        ChatMessages chatMessages = mongoTemplate.findOne(query, ChatMessages.class);
        // 查不到说明是新会话，返回空列表
        if(chatMessages == null) return new LinkedList<>();
        // 将数据库中存储的 JSON 字符串反序列化为消息对象列表
        return ChatMessageDeserializer.messagesFromJson(chatMessages.getContent());
    }

    /**
     * 保存/更新指定会话的消息列表。
     * 框架在每次对话后调用，传入的是该会话当前完整的消息列表。
     *
     * @param memoryId 会话唯一标识
     * @param messages 当前会话的完整消息列表
     */
    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        // 构造查询条件：定位该会话的文档
        Criteria criteria = Criteria.where("memoryId").is(memoryId);
        Query query = new Query(criteria);
        // 将消息列表序列化为 JSON 字符串，写入 content 字段
        Update update = new Update();
        update.set("content", ChatMessageSerializer.messagesToJson(messages));
        //根据query条件能查询出文档，则修改文档；否则新增文档（upsert = update + insert）
        mongoTemplate.upsert(query, update, ChatMessages.class);
    }

    /**
     * 删除指定会话的全部消息（清空该会话记忆）。
     *
     * @param memoryId 会话唯一标识
     */
    @Override
    public void deleteMessages(Object memoryId) {
        // 构造查询条件并删除对应文档
        Criteria criteria = Criteria.where("memoryId").is(memoryId);
        Query query = new Query(criteria);
        mongoTemplate.remove(query, ChatMessages.class);
    }
}
