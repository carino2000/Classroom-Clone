package com.example.app.classroom.mapper;

import com.example.app.classroom.domain.Verification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VerificationMapper {
    int insertOne(Verification verification);

    Verification selectLatestByMemberId(String memberId);
}
