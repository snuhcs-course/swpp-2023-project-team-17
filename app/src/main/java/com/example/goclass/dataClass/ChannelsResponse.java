package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class ChannelsResponse {
    @SerializedName("class_id")
    String classId;
    @SerializedName("channel_type")
    String channelType;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;


    public String getClassId() {
        return classId;
    }

    public String getChannelType() {
        return channelType;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}