package com.example.goclass.network.dataclass;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ClassListsResponse {
    @SerializedName("classList")
    private List<ClassesResponse> classList;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public ClassListsResponse(List<ClassesResponse> classList, int code, String message) {
        this.classList = classList;
        this.code = code;
        this.message = message;
    }
    public List<ClassesResponse> getClassList() {
        return classList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}