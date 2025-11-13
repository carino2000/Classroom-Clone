package com.example.app.classroom.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Verification {
    String id;
    String memberId;
    String code;
    LocalDateTime expiredAt;

    public Verification() {}

    public Verification(String code) {
        this.code = code;
    }
}
