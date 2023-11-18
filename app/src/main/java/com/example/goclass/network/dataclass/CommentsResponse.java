package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class CommentsResponse {
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

    public CommentsResponse(int messageId, int commentId, int classId, String timeStamp, int senderId, String senderName, String content) {
        this.messageId = messageId;
        this.commentId = commentId;
        this.classId = classId;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
    }

    public CommentsResponse(int classId, int commentId, String senderName, String content, String timeStamp) {
        this.classId = classId;
        this.commentId = commentId;
        this.senderName = senderName;
        this.content = content;
        this.timeStamp = timeStamp;
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
