package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class AttendancesResponse {
    @SerializedName("attendance_date")
    String attendanceDate;
    @SerializedName("attendance_status")
    int attendanceStatus;
    @SerializedName("attendance_duration")
    int attendanceDuration;
    @SerializedName("is_sent")
    int isSent;
    @SerializedName("student_id")
    int studentId;
    @SerializedName("class_id")
    int classId;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public int getAttendanceStatus() {
        return attendanceStatus;
    }

    public int getAttendanceDuration() {
        return attendanceDuration;
    }

    public int getIsSent() {
        return isSent;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getClassId() {
        return classId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}