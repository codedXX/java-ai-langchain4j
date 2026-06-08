package com.dyx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("chat_messages")
public class ChatMessages {
    //唯一标识，映射到 MongoDB 文档的 _id 字段
    @Id
    private ObjectId id;// MongoDB 文档主键 _id
    private int messageId;// 业务上的会话 id(其实就是 memoryId)
    private String content; //关键:把整个聊天记录列表序列化成 JSON 字符串存这里
}