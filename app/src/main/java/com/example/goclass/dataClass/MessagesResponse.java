package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class MessagesResponse {
    @SerializedName("message_id")
    String messageId;
    @SerializedName("time_stamp")
    String timeStamp;
    @SerializedName("sender_id")
    String senderId;
    @SerializedName("content")
    String content;
    @SerializedName("channel_id")
    String channelId;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public String getMessageId() {
        return messageId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public String getChannelId() {
        return channelId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}