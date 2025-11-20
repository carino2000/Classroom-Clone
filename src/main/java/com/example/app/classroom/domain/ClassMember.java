package com.example.app.classroom.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassMember {
    int id;
    String studentId;
    String classroomId;
    String blocked;

    public ClassMember() {}

    public ClassMember(String studentId, String classroomId) {
        this.studentId = studentId;
        this.classroomId = classroomId;
    }
}
