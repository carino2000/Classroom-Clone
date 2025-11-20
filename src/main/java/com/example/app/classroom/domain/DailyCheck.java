package com.example.app.classroom.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailyCheck {

    int id;
    int classMemberId;
    LocalDate checkedAt;

    public DailyCheck(int classMemberId) {
        this.classMemberId = classMemberId;
        this.checkedAt = LocalDate.now();
    }
}
