package com.example.app.classroom.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateNoticeRequest(String content, List<MultipartFile> attachment) {
}
