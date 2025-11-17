package com.example.app.classroom.mapper;

import com.example.app.classroom.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {
    int insertOne(Member member);

    Member selectById(String id);

    int updateActiveById(String id);
    int updateImageById(Map<String,String> map);
    List<Member> selectByIds(List<String> memberId);
}
