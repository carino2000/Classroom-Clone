package com.example.app.classroom.controller;

import com.example.app.classroom.domain.Member;
import com.example.app.classroom.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ajax")
@RequiredArgsConstructor
public class AjaxController {
    final MemberMapper memberMapper;

    @GetMapping("/signup/id-check")
    @ResponseBody
    public String signupIdCheckHandle(@RequestParam String id) {
        Member found = memberMapper.selectById(id);
        if (found != null) {
            return "duplicated";
        }else{
            return "available";
        }
    }
}
