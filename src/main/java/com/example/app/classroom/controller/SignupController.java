package com.example.app.classroom.controller;

import com.example.app.classroom.domain.Member;
import com.example.app.classroom.dto.AccountCreationRequest;
import com.example.app.classroom.dto.ProfileSetupRequest;
import com.example.app.classroom.mapper.MemberMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {
    final MemberMapper memberMapper;
    //final JavaMailSender mailSender;

    @GetMapping("/step/account")
    public String signupStep1GetHandler() {
        return "signup/account";
    }

    @PostMapping("/step/account")
    public String signupStep1PostHandler(@ModelAttribute AccountCreationRequest acr, HttpSession session) {
        Member temporalMember = new Member(acr.id(), acr.pw(), acr.email());
        session.setAttribute("temporalMember", temporalMember);
        return "redirect:/signup/step/profile";
    }

    @GetMapping("/step/profile")
    public String signupStep2GetHandler() {
        return "signup/profile";
    }

    @PostMapping("/step/profile")
    public String signupStep2PostHandler(@ModelAttribute ProfileSetupRequest psr,
                                         @SessionAttribute Member temporalMember) { // == Member temporalMember = (Member)(session.getAttribute("temporalMember"));
        temporalMember.setName(psr.name());
        temporalMember.setBirthday(LocalDate.of(psr.year(), psr.month(), psr.date()));
        temporalMember.setGender(psr.gender());

        memberMapper.insertOne(temporalMember);
        return "redirect:/index";
    }



}
