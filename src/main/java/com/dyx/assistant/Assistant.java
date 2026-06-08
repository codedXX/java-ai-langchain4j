package com.dyx.assistant;

import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

/**
 * 自定义 AiService 接口
 * 由 LangChain4j 的 AiServices 动态生成实现
 */
@AiService(wiringMode = EXPLICIT, chatModel = "qwenChatModel")
public interface Assistant {

    /**
     * 与大模型对话
     *
     * @param message 用户输入
     * @return 模型回复
     */
    String chat(String message);
}
