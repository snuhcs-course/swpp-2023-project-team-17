package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class ClassCreateResponse {
    @SerializedName("classId")
    private int classId;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getClassId() { return  classId; }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ClassCreateResponse(int classId, int code, String message){
        this.classId = classId;
        this.code = code;
        this.message = message;
    }
}
