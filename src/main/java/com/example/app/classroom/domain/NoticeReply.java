package com.example.app.classroom.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeReply {
    int id;
    int noticeId;
    String writerId;
    String comment;
    LocalDateTime createdAt;

    public NoticeReply() {}

    public NoticeReply(int noticeId, String writerId, String comment) {
        this.noticeId = noticeId;
        this.writerId = writerId;
        this.comment = comment;
    }
}
