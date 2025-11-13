package com.example.app.classroom.controller;

import com.example.app.classroom.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/index")
@RequiredArgsConstructor
public class IndexController {
    @GetMapping
    public String indexHandler(@SessionAttribute(required = false) Member logonMember) {
        if(logonMember == null){
            return "redirect:/login";
        }else if(!logonMember.isActive()){
            return "redirect:/verify/send/email";
        }else{
            return "index";
        }

    }
}
