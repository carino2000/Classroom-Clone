package com.example.app.classroom.mapper;

import com.example.app.classroom.domain.Notice;
import com.example.app.classroom.domain.NoticeAttachment;
import com.example.app.classroom.domain.NoticeReply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    int insertNotice(Notice notice);
    int insertAttachment(NoticeAttachment noticeAttachment);
    int insertReply(NoticeReply noticeReply);


    List<Notice> selectNoticeByClassroomId(String classroomId);
    List<NoticeAttachment> selectAttachmentByNoticeId(int noticeId);
    List<NoticeReply> selectReplyByNoticeId(int noticeId);


    int deleteReplyById(int replyId);
}
