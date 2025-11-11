package com.example.app.classroom.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Member {
    String id;
    String pw;
    String email;
    String name;
    String imageUrl;
    LocalDate birthday;
    String gender;
    boolean active;

    public Member() {}

    public Member(String id, String pw, String email) {
        this.id = id;
        this.pw = pw;
        this.email = email;
    }


}
