package com.dyx;

import com.dyx.assistant.SeparateChatAssistant;
import com.dyx.assistant.XiaozhiAgent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ToolsTest {
    @Autowired
    private SeparateChatAssistant separateChatAssistant;
    @Autowired
    private XiaozhiAgent xiaozhiAgent;

    @Test
    public void testCalculatorTools() {
        String answer = separateChatAssistant.chat(1, "1+2等于几，475695037565的平方根是多少？");
        //答案：3，689706.4865
        System.out.println(answer);
    }
    @Test
    public void testCalculatorTools2() {
        String answer = xiaozhiAgent.chat(2, "1+2等于几，475695037565的平方根是多少？");
        //答案：3，689706.4865
        System.out.println(answer);
    }

    /**
     * 测试 RAG：问一个只有知识库里才有答案的问题，验证 Redis 向量检索是否生效
     */
    @Test
    public void testXiaozhiRag() {
        String answer = xiaozhiAgent.chat(100, "北京协和医院的门诊开放时间是什么时候？地址在哪里？");
        System.out.println(answer);
    }
}