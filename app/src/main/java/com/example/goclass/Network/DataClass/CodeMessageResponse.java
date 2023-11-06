package com.example.goclass.Network.DataClass;
import com.google.gson.annotations.SerializedName;

public class CodeMessageResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public CodeMessageResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
