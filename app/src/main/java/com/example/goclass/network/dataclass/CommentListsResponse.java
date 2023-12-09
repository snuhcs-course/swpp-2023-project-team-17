package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentListsResponse {
    @SerializedName("messageList")
    private List<CommentsResponse> commentList;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public CommentListsResponse(List<CommentsResponse> commentList, int code, String message) {
        this.commentList = commentList;
        this.code = code;
        this.message = message;
    }

    public CommentListsResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public List<CommentsResponse> getCommentList() {
        return commentList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
