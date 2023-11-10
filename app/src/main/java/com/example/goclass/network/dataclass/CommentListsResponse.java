package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentListsResponse {
    @SerializedName("messageList")
    private List<Comments> commentList;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public List<Comments> getCommentList() {
        return commentList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
