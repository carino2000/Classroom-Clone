package com.example.app.classroom.controller;

import com.example.app.classroom.domain.ClassMember;
import com.example.app.classroom.domain.DailyCheck;
import com.example.app.classroom.domain.Member;
import com.example.app.classroom.domain.Verification;
import com.example.app.classroom.mapper.ClassMemberMapper;
import com.example.app.classroom.mapper.MemberMapper;
import com.example.app.classroom.mapper.VerificationMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/ajax")
@RequiredArgsConstructor
public class AjaxController {
    final MemberMapper memberMapper;
    final VerificationMapper verificationMapper;
    final JavaMailSender mailSender;
    final ClassMemberMapper classMemberMapper;

    @GetMapping("/signup/id-check")
    @ResponseBody
    public String signupIdCheckHandle(@RequestParam String id) {
        Member found = memberMapper.selectById(id);
        if (found != null) {
            return "duplicated";
        } else {
            return "available";
        }
    }

    @PostMapping("/send/email")
    @ResponseBody
    public String authCodeSendHandle(@SessionAttribute Member logonMember) {

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
                "인증코드 : [" + code + "]\n\n" +
                "감사합니다."
        );

        mailSender.send(message);

        return "success";
    }


    @GetMapping("/check/email")
    @ResponseBody
    public String authCodeHandle(@SessionAttribute Member logonMember,
                                 @RequestParam String code) {
        Verification verification = verificationMapper.selectLatestByMemberId(logonMember.getId());

        boolean isExpired = verification.getExpiredAt().isBefore(LocalDateTime.now());
        boolean isVerified = verification.getCode().equals(code);

        if (!isVerified || isExpired) {
            return "unAuth";
        }

        return "auth";
    }


    @PostMapping("/classroom/{classroomId}/daily-check")
    @ResponseBody
    public String dailyCheckHandle(@PathVariable String classroomId, @SessionAttribute Member logonMember) {

        Map<String, String> map = Map.of("studentId", logonMember.getId(), "classroomId", classroomId);
        ClassMember classMember = classMemberMapper.selectByStudentAndClassId(map);
        if (classMember == null) {
            return "error";
        }

        boolean r = classMemberMapper.existTodayCheckByClassMemberId(classMember.getId());
        if (r) {
            return "already_checked";
        } else {
            DailyCheck dailyCheck = new DailyCheck(classMember.getId());
            classMemberMapper.insertDailyCheck(dailyCheck);
            return "checked";
        }
    }

}
