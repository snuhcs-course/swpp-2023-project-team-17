package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Attendances {
    @SerializedName("attendanceStatus")
    int attendanceStatus = -1;
    @SerializedName("attendanceDuration")
    int attendanceDuration = -1;
    @SerializedName("classId")
    int classId = -1;
    @SerializedName("attendanceDetail")
    String attendanceDetail;

    public Attendances(int attendanceStatus, int attendanceDuration, int classId) {
        this.attendanceStatus = attendanceStatus;
        this.attendanceDuration = attendanceDuration;
        this.classId = classId;
    }

    public Attendances(int attendanceStatus, int attendanceDuration, int classId, String attendanceDetailList) {
        this.attendanceStatus = attendanceStatus;
        this.attendanceDuration = attendanceDuration;
        this.classId = classId;
        this.attendanceDetail = attendanceDetailList;
    }
}