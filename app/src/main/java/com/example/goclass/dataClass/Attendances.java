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

    public Attendances(String attendanceStatus, String attendanceDuration, String studentId, String classId) {
        this.attendanceStatus = attendanceStatus;
        this.attendanceDuration = attendanceDuration;
        this.studentId = studentId;
        this.classId = classId;
    }

    public Attendances(String attendanceStatus, String attendanceDuration, String classId) {
        this.attendanceStatus = attendanceStatus;
        this.attendanceDuration = attendanceDuration;
        this.classId = classId;
    }
}