package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Teaches {
    @SerializedName("professorId")
    int professorId;
    @SerializedName("classId")
    int classId;

    public Teaches(int professorId, int classId) {
        this.professorId = professorId;
        this.classId = classId;
    }
}