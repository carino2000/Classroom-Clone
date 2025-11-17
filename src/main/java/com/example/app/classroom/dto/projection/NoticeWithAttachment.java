package com.example.app.classroom.dto.projection;

import com.example.app.classroom.domain.Notice;
import com.example.app.classroom.domain.NoticeAttachment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NoticeWithAttachment extends Notice{
    List<NoticeAttachment> attachments;

    public NoticeWithAttachment() {
    }

    public NoticeWithAttachment(String classroomId, String content) {
        super(classroomId, content);
    }
}
