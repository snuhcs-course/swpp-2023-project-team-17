package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class AttendancesResponse {
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

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public String getAttendanceDuration() {
        return attendanceDuration;
    }

    public String getIsSent() {
        return isSent;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getClassId() {
        return classId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}