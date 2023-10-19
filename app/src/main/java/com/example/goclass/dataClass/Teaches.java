package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Teaches {
    @SerializedName("professor_id")
    String professorId;
    @SerializedName("class_id")
    String classId;

    public Teaches(String professorId, String classId) {
        this.professorId = professorId;
        this.classId = classId;
    }
}