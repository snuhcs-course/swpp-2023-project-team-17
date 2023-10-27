package com.example.goclass.dataClass;
import java.util.HashMap;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MessageListsResponse {
    @SerializedName("messageList")
    private List<HashMap<String, String>> messageList;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public List<HashMap<String, String>> getMessageId() {
        return messageList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}