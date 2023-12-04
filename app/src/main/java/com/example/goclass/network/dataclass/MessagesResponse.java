package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class MessagesResponse {
    @SerializedName("messageId")
    int messageId;
    @SerializedName("commentId")
    int commentId;
    @SerializedName("classId")
    int classId;
    @SerializedName("timeStamp")
    String timeStamp;
    @SerializedName("senderId")
    int senderId;
    @SerializedName("senderName")
    String senderName;
    @SerializedName("content")
    String content;

    public MessagesResponse(int messageId, int commentId, int classId, String timeStamp, int senderId, String senderName, String content) {
        this.messageId = messageId;
        this.commentId = commentId;
        this.classId = classId;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
    }

    public MessagesResponse(int classId, int commentId, String senderName, String content, String timeStamp) {
        this.classId = classId;
        this.commentId = commentId;
        this.senderName = senderName;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getCommentId() {
        return commentId;
    }

    public int getClassId() {
        return classId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getContent() {
        return content;
    }
}