package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Teaches {
    @SerializedName("professor_id")
    int professorId;
    @SerializedName("class_id")
    int classId;

    public Teaches(int professorId, int classId) {
        this.professorId = professorId;
        this.classId = classId;
    }
}