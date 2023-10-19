package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Attendances {
    @SerializedName("attendance_date")
    String attendanceDate;
    @SerializedName("attendance_status")
    String attendanceStatus;
    @SerializedName("attendance_duration")
    String attendanceDuration;
    @SerializedName("is_sent")
    String isSent;
    @SerializedName("student_id")
    String studentId;
    @SerializedName("class_id")
    String classId;

    public Attendances(String attendanceDate, String attendanceStatus, String attendanceDuration, String isSent, String studentId, String classId) {
        this.attendanceDate = attendanceDate;
        this.attendanceStatus = attendanceStatus;
        this.attendanceDuration = attendanceDuration;
        this.isSent = isSent;
        this.classId = classId;
        this.studentId = studentId;
    }

}