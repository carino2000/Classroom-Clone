package com.example.app.classroom.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Classroom {
    String id;
    String teacherId;
    String name;
    String description;
    String joinCode;
    String theme;

    public Classroom() {}

    public Classroom(String teacherId, String name, String description) {
        this.teacherId = teacherId;
        this.name = name;
        this.description = description;
    }

    public Classroom(String id, String teacherId, String name, String description, String joinCode) {
        this.id = id;
        this.teacherId = teacherId;
        this.name = name;
        this.description = description;
        this.joinCode = joinCode;
    }
}
