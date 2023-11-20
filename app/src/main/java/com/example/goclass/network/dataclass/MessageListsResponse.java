package com.example.goclass.network.dataclass;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MessageListsResponse {
    @SerializedName("messageList")
    private List<MessagesResponse> messageList;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public MessageListsResponse(List<MessagesResponse> messageList, int code, String message) {
        this.messageList = messageList;
        this.code = code;
        this.message = message;
    }

    public List<MessagesResponse> getMessageList() {
        return messageList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}