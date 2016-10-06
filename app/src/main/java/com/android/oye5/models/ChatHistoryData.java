package com.android.oye5.models;

import java.io.Serializable;

public class ChatHistoryData implements Serializable {
    public static final int CHAT_TYPE_IN = 1;
    public static final int CHAT_TYPE_OUT = 2;
    private String messageContent = "";
    private String messageTime = "";
    private int messageType = CHAT_TYPE_IN;

    public ChatHistoryData() {
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
