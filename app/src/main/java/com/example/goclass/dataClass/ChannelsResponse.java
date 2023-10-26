package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class ChannelsResponse {
    @SerializedName("class_id")
    int classId;
    @SerializedName("channel_type")
    int channelType;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;


    public int getClassId() {
        return classId;
    }

    public int getChannelType() {
        return channelType;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}