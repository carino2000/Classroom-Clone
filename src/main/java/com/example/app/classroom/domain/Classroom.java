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
}
