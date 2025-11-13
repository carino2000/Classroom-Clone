package com.example.app.classroom.controller;

import com.example.app.classroom.domain.Member;
import com.example.app.classroom.domain.Verification;
import com.example.app.classroom.dto.AccountCreationRequest;
import com.example.app.classroom.dto.ProfileSetupRequest;
import com.example.app.classroom.mapper.MemberMapper;
import com.example.app.classroom.mapper.VerificationMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {
    final MemberMapper memberMapper;

    @GetMapping("/step/account")
    public String signupStep1GetHandler(@ModelAttribute("acr") AccountCreationRequest acr,
                                        Model model) {
        return "signup/account";
    }


    @PostMapping("/step/account")
    public String signupStep1PostHandler(@ModelAttribute AccountCreationRequest acr,
                                         HttpSession session,
                                         RedirectAttributes ra) {
        Member found = memberMapper.selectById(acr.id());
        if (found != null) {
            ra.addFlashAttribute("duplicate", true);
            ra.addFlashAttribute("acr", acr);
            return "redirect:/signup/step/account";
        }


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Member temporalMember = new Member(acr.id(), passwordEncoder.encode(acr.pw()), acr.email());
        session.setAttribute("temporalMember", temporalMember);
        return "redirect:/signup/step/profile";
    }

    @GetMapping("/step/profile")
    public String signupStep2GetHandler() {
        return "signup/profile";
    }

    @PostMapping("/step/profile")
    @Transactional //해당 공간에서 실시하는 모든 DB작업을 취합한 뒤, 오류 안나면 커밋, 오류나면 롤백
    public String signupStep2PostHandler(@ModelAttribute ProfileSetupRequest psr,
                                         @SessionAttribute Member temporalMember) { // == Member temporalMember = (Member)(session.getAttribute("temporalMember"));
        temporalMember.setName(psr.name());
        temporalMember.setBirthday(LocalDate.of(psr.year(), psr.month(), psr.date()));
        temporalMember.setGender(psr.gender());
        memberMapper.insertOne(temporalMember);

        return "redirect:/login";
    }




}
