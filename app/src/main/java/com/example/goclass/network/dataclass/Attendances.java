package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class Attendances {
    @SerializedName("attendanceStatus")
    int attendanceStatus = -1;
    @SerializedName("attendanceDuration")
    int attendanceDuration = -1;
    @SerializedName("classId")
    int classId = -1;

    public Attendances(int attendanceStatus, int attendanceDuration, int classId) {
        this.attendanceStatus = attendanceStatus;
        this.attendanceDuration = attendanceDuration;
        this.classId = classId;
    }
}