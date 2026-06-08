package com.dyx.pojo;

import lombok.Data;

@Data
public class ChatForm {
    private int memoryId;//对话id
    private String message;//用户问题
}