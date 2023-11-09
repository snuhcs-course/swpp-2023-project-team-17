package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class Attendances {
    @SerializedName("attendanceStatus")
    int attendanceStatus = -1;
    @SerializedName("attendanceDuration")
    int attendanceDuration = -1;
    @SerializedName("classId")
    int classId = -1;
    @SerializedName("studentId")
    int studentId = -1;
    @SerializedName("attendanceDate")
    String attendanceDate = "";

    public Attendances(int attendanceStatus, int attendanceDuration, int studentId, int classId) {
        this.attendanceStatus = attendanceStatus;
        this.attendanceDuration = attendanceDuration;
        this.studentId = studentId;
        this.classId = classId;
    }

    public Attendances(int attendanceStatus, int attendanceDuration, int classId) {
        this.attendanceStatus = attendanceStatus;
        this.attendanceDuration = attendanceDuration;
        this.classId = classId;
    }

    public int getAttendanceStatus() {
        return attendanceStatus;
    }
}