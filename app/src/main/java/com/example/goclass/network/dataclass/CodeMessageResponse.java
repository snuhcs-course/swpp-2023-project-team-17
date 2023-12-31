package com.example.goclass.network.dataclass;
import com.google.gson.annotations.SerializedName;

public class CodeMessageResponse {
    @SerializedName("code")
    private int code;

    public CodeMessageResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
