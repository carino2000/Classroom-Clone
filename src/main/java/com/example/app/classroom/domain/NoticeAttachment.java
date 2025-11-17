package com.example.app.classroom.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeAttachment {
    int id;
    int noticeId;
    String fileName;
    long fileSize;
    String fileUrl;
    String fileContentType;

}
