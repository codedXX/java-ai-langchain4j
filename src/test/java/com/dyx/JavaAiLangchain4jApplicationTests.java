package com.dyx;

import com.dyx.assistant.Assistant;
import com.dyx.assistant.MemoryChatAssistant;
import com.dyx.assistant.SeparateChatAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavaAiLangchain4jApplicationTests {

    /**
     * 通义千问大模型
     */
    @Autowired
    private QwenChatModel qwenChatModel;

    @Test
    public void testDashScopeQwen() {
        //向模型提问
        String answer = qwenChatModel.chat("你好");
        //输出结果
        System.out.println(answer);
    }

    @Test
    public void testDashScopeWanx(){
        WanxImageModel wanxImageModel = WanxImageModel.builder()
                .modelName("wanx2.1-t2i-plus")
                .apiKey("sk-255506ca196b48f38e686b3e82efac58")
                .build();
        Response<Image> response = wanxImageModel.generate("奇幻森林精灵：在一片弥漫着轻柔薄雾的古老森林深处，阳光透过茂密枝叶洒下金色光斑。一位身材娇小、长着透明薄翼的精灵少女站在一朵硕大的蘑菇上。她有着海藻般的绿色长发，发间点缀着蓝色的小花，皮肤泛着珍珠般的微光。身上穿着由翠绿树叶和白色藤蔓编织而成的连衣裙，手中捧着一颗散发着柔和光芒的水晶球，周围环绕着五彩斑斓的蝴蝶，脚下是铺满苔藓的地面，蘑菇和蕨类植物丛生，营造出神秘而梦幻的氛围。");
        System.out.println(response.content().url());
    }

    @Autowired
    private Assistant assistant;

    @Test
    public void testA(){
        String answer1 = assistant.chat("我是环环");
        System.out.println(answer1);
        String answer2 = assistant.chat("我是谁");
        System.out.println(answer2);
    }

    @Test
    public void testChatMemory3() {
        //创建chatMemory
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        //创建AIService
        Assistant assistant = AiServices
                .builder(Assistant.class)
                .chatModel(qwenChatModel)
                .chatMemory(chatMemory)
                .build();
        //调用service的接口
        String answer1 = assistant.chat("我是环环");
        System.out.println(answer1);
        String answer2 = assistant.chat("请问我是谁？");
        System.out.println(answer2);
    }

    @Autowired
    private MemoryChatAssistant memoryChatAssistant;
    @Test
    public void testChatMemory4() {
        //调用service的接口
        String answer1 = memoryChatAssistant.chat("我是环环");
        System.out.println("问1："+answer1);
        String answer2 = memoryChatAssistant.chat("请问我是谁？");
        System.out.println("问2："+answer2);
    }

    @Autowired
    private SeparateChatAssistant separateChatAssistant;
    @Test
    public void testChatMemor5() {
        //调用service的接口
        String answer1 = separateChatAssistant.chat(1,"我是环环");
        System.out.println("问1："+answer1);
        String answer2 = separateChatAssistant.chat(1,"我是谁");
        System.out.println("问2："+answer2);
        String answer3 = separateChatAssistant.chat(2,"我是谁");
        System.out.println("问3："+answer3);
    }
    //    @Autowired
//    private OpenAiChatModel openAiChatModel;
//    private ChatModel chatModel;
//
//
//    @Test
//    void contextLoads() {
//    }
//
//    @Test
//    void testLLM(){
//        //向模型提问
//        String answer = chatModel.chat("你好");
//        //输出结果
//        System.out.println(answer);
//    }
}
