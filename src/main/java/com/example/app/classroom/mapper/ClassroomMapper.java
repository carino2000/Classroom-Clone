package com.example.app.classroom.mapper;

import com.example.app.classroom.domain.Classroom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClassroomMapper {
    int insertOne(Classroom classroom);

    Classroom selectById(String id);
    Classroom selectByJoinCode(String joinCode);
}
