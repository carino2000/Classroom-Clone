package com.example.app.classroom.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Notice {
    int id;
    String classroomId;
    String content;
    LocalDateTime createdAt;
}
