package com.example.goclass.dataClass;
import java.util.HashMap;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ClassListsResponse {
    @SerializedName("classList")
    private List<Classes> classList;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public ClassListsResponse(List<Classes> classList, int code, String message) {
        this.classList = classList;
        this.code = code;
        this.message = message;
    }
    public List<Classes> getClassList() {
        return classList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}