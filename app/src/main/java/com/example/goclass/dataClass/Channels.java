package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Channels {
    @SerializedName("channel_id")
    String channelId;
    @SerializedName("class_id")
    String classId;
    @SerializedName("channel_type")
    String channelType;

    public Channels(String channelId, String classId, String channelType) {
        this.channelId = channelId;
        this.classId = classId;
        this.channelType = channelType;
    }
}