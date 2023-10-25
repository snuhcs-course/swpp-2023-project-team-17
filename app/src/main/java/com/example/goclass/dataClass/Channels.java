package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Channels {
    @SerializedName("channel_id")
    int channelId;
    @SerializedName("class_id")
    int classId;
    @SerializedName("channel_type")
    int channelType;

    public Channels(int channelId, int classId, int channelType) {
        this.channelId = channelId;
        this.classId = classId;
        this.channelType = channelType;
    }
}