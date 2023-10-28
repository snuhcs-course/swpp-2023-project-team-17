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