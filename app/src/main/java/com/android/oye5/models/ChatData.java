package com.android.oye5.models;

import java.io.Serializable;

public class ChatData implements Serializable {
    private String userName = "";
    private String productName = "";
    private String chatTime = "";

    public ChatData() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }
}
