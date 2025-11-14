package com.example.app.classroom.mapper;

import com.example.app.classroom.domain.Classroom;
import com.example.app.classroom.dto.projection.ClassroomWithTeacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassroomMapper {
    int insertOne(Classroom classroom);

    Classroom selectById(String id);
    Classroom selectByJoinCode(String joinCode);
    List<ClassroomWithTeacher> selectAllMyClassroomById(String id);
}
