package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class Comments {
    @SerializedName("messageId")
    int messageId = -1;
    @SerializedName("classId")
    int classId = -1;
    @SerializedName("timeStamp")
    String timeStamp = "";
    @SerializedName("senderId")
    int senderId = -1;
    @SerializedName("content")
    String content = "";
    @SerializedName("commentId")
    int commentId = -1;
    @SerializedName("senderName")
    String senderName = "";

    public Comments(int senderId, String content) {
        this.senderId = senderId;
        this.content = content;
    }

    public Comments(String content, int messageId) {
        this.messageId = messageId;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getClassId() {
        return classId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getCommentId() {
        return commentId;
    }
}
