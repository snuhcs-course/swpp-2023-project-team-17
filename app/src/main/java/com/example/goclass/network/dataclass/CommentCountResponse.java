package com.example.goclass.network.dataclass;
import com.google.gson.annotations.SerializedName;

public class CommentCountResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("count")
    private int count;

    public CommentCountResponse(int code, String message, int count) {
        this.code = code;
        this.message = message;
        this.count = count;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getCount() {
        return count;
    }
}
