package com.example.app.classroom.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/logout")
public class LogoutController {
    @GetMapping
    public String loginGetHandle(HttpSession session) {
        if (session.getAttribute("logonMember") != null) {
            session.removeAttribute("logonMember");
            //session.invalidate(); // 세션 그릇 자체를 지워버림
        }
        return "redirect:/index";
    }
}
