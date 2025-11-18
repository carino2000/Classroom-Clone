package com.example.app.classroom.dto.projection;

import com.example.app.classroom.domain.Notice;
import com.example.app.classroom.domain.NoticeAttachment;
import com.example.app.classroom.domain.NoticeReply;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NoticeWithAttachment extends Notice{
    List<NoticeAttachment> attachments;
    List<NoticeReply> replies;

    public NoticeWithAttachment() {
    }

    public NoticeWithAttachment(String classroomId, String content) {
        super(classroomId, content);
    }
}
