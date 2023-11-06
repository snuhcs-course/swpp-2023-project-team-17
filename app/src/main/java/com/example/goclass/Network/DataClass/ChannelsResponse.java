package com.example.goclass.Network.DataClass;

import com.google.gson.annotations.SerializedName;

public class ChannelsResponse {
    @SerializedName("classId")
    int classId;
    @SerializedName("channelType")
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