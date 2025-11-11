package com.example.app.classroom.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassMember {
    String id;
    String studentId;
    String classroomId;
    String blocked;

    public ClassMember() {}
}
