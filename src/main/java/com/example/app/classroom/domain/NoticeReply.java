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
    String content;
    LocalDateTime createdAt;
}
