package com.example.app.classroom.controller;

import com.example.app.classroom.domain.Member;
import com.example.app.classroom.dto.LoginRequest;
import com.example.app.classroom.mapper.MemberMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    final MemberMapper memberMapper;

    @GetMapping
    public String loginGetHandler(@SessionAttribute(required = false) Member logonMember) {
        if (logonMember != null) {
            return "redirect:/index";
        }
        return "signup/login";
    }

    @PostMapping
    public String loginPostHandler(@ModelAttribute LoginRequest loginRequest,
                                   HttpSession session) {
        Member found = memberMapper.selectById(loginRequest.id());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (found == null || !passwordEncoder.matches(loginRequest.pw(), found.getPw())) {
            return "redirect:/login";
        }

        session.setAttribute("logonMember", found);
        if(!found.isActive()){
            return "redirect:/verify/send/email";
        }

        return "redirect:/index";
    }
}
