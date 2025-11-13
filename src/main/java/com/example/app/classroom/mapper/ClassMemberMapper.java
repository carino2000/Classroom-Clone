package com.example.app.classroom.mapper;

import com.example.app.classroom.domain.ClassMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ClassMemberMapper {
    int insertOne(ClassMember classMember);

    ClassMember selectByStudentAndClassId(Map<String,String> map);

}
