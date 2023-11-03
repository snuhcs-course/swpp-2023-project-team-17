package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Channels {
    @SerializedName("channelId")
    int channelId;
    @SerializedName("classId")
    int classId;
    @SerializedName("channelType")
    int channelType;

    public Channels(int channelId, int classId, int channelType) {
        this.channelId = channelId;
        this.classId = classId;
        this.channelType = channelType;
    }
}