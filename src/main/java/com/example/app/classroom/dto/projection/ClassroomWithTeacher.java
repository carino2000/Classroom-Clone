package com.example.app.classroom.dto.projection;

import com.example.app.classroom.domain.Classroom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassroomWithTeacher extends Classroom {
    Boolean teacher;
}
