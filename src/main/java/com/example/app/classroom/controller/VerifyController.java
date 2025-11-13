package com.example.app.classroom.controller;

import com.example.app.classroom.domain.Member;
import com.example.app.classroom.domain.Verification;
import com.example.app.classroom.mapper.MemberMapper;
import com.example.app.classroom.mapper.VerificationMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/verify")
public class VerifyController {
    final VerificationMapper verificationMapper;
    final MemberMapper memberMapper;
    final JavaMailSender mailSender;

    @GetMapping("/send/email")
    public String authCodeSend(@SessionAttribute Member logonMember) {
        int rand = (int) (Math.random() * 1000000);
        String code = String.format("%06d", rand);

        Verification verification = new Verification();
        verification.setCode(code);
        verification.setMemberId(logonMember.getId());
        verification.setExpiredAt(LocalDateTime.now().plusHours(1));
        verificationMapper.insertOne(verification);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(logonMember.getEmail());
        message.setSubject("[클래스룸] 이메일 인증을 완료해주세요");
        message.setText("안녕하세요." + logonMember.getName() + "님!\n\n" +
                "아래 인증 코드를 입력해 인증을 완료해주세요!\n\n" +
                "인증코드 : " + code + "\n\n" +
                "감사합니다."
        );

        mailSender.send(message);

        return "redirect:/verify/email";
    }

    @GetMapping("/email")
    public String verifyEmailGetHandler(@SessionAttribute(required = false) Member logonMember,
                                        Model model) {
        if (logonMember == null) {
            return "redirect:/login";
        }

        return "signup/verify-email";
    }

    @PostMapping("/email")
    public String verifyEmailPostHandler(@RequestParam String code,
                                         @SessionAttribute(required = false) Member logonMember,
                                         RedirectAttributes ra,
                                         HttpSession session) {
        if (logonMember == null) {
            return "redirect:/login";
        }
        System.out.println(code);

        Verification verification = verificationMapper.selectLatestByMemberId(logonMember.getId());

        boolean isExpired = verification.getExpiredAt().isBefore(LocalDateTime.now());
        boolean isVerified = verification.getCode().equals(code);

        if(!isVerified || isExpired) {
            ra.addFlashAttribute("isVerified", false);
            return "redirect:/verify/email";
        }

        System.out.println("Email has been verified !");
        memberMapper.updateActiveById(logonMember.getId());
        session.setAttribute("logonMember", memberMapper.selectById(logonMember.getId()));
        return "redirect:/index";
    }
}
