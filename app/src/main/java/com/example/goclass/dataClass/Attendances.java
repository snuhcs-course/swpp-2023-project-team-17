package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Attendances {
    @SerializedName("attendance_status")
    int attendanceStatus = -1;
    @SerializedName("attendance_duration")
    int attendanceDuration = -1;
    @SerializedName("class_id")
    int classId = -1;
    @SerializedName("student_id")
    int studentId = -1;

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
}