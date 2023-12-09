package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class AttendancesResponse {
    @SerializedName("attendanceId")
    int attendanceId;
    @SerializedName("attendanceDate")
    String attendanceDate;
    @SerializedName("attendanceStatus")
    int attendanceStatus;
    @SerializedName("attendanceDuration")
    int attendanceDuration;
    @SerializedName("isSent")
    int isSent;
    @SerializedName("studentId")
    int studentId;
    @SerializedName("userName")
    String studentName;
    @SerializedName("classId")
    int classId;
    @SerializedName("attendanceDetail")
    String attendanceDetail;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public AttendancesResponse(int attendanceId, int attendanceStatus, String attendanceDate, int attendanceDuration, int isSent, int studentId, String studentName, int classId, String attendanceDetail, int code, String message) {
        this.attendanceId = attendanceId;
        this.attendanceStatus = attendanceStatus;
        this.attendanceDate = attendanceDate;
        this.attendanceDuration = attendanceDuration;
        this.isSent = isSent;
        this.studentId = studentId;
        this.studentName = studentName;
        this.classId = classId;
        this.attendanceDetail = attendanceDetail;
        this.code = code;
        this.message = message;
    }

    public AttendancesResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public AttendancesResponse(int attendanceId, int attendanceStatus, String attendanceDate, int attendanceDuration, int isSent, int studentId, String studentName, int classId, String attendanceDetail) {
        this.attendanceId = attendanceId;
        this.attendanceStatus = attendanceStatus;
        this.attendanceDate = attendanceDate;
        this.attendanceDuration = attendanceDuration;
        this.isSent = isSent;
        this.studentId = studentId;
        this.studentName = studentName;
        this.classId = classId;
        this.attendanceDetail = attendanceDetail;
    }

    public AttendancesResponse(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

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

    public String getStudentName() {
        return studentName;
    }

    public String getAttendanceDetail() {
        return attendanceDetail;
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