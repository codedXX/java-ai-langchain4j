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
}