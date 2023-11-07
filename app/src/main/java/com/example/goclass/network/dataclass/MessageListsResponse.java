package com.example.goclass.network.dataclass;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MessageListsResponse {
    @SerializedName("messageList")
    private List<Messages> messageList;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public List<Messages> getMessageId() {
        return messageList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}