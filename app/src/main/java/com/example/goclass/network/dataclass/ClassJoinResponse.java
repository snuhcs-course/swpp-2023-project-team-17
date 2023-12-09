package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class ClassJoinResponse {
    @SerializedName("classId")
    private int classId;
    @SerializedName("classTime")
    private String classTime;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getClassId() { return  classId; }

    public String getClassTime() { return classTime; }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ClassJoinResponse(int classId, String classTime, int code, String message){
        this.classId = classId;
        this.classTime = classTime;
        this.code = code;
        this.message = message;
    }

    public ClassJoinResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
