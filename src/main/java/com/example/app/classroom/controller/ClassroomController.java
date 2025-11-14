package com.example.app.classroom.controller;

import com.example.app.classroom.domain.ClassMember;
import com.example.app.classroom.domain.Classroom;
import com.example.app.classroom.domain.Member;
import com.example.app.classroom.dto.request.CreateClassroomRequest;
import com.example.app.classroom.dto.request.CreateNoticeRequest;
import com.example.app.classroom.mapper.ClassMemberMapper;
import com.example.app.classroom.mapper.ClassroomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/classroom")
public class ClassroomController {
    final ClassroomMapper classroomMapper;
    final ClassMemberMapper classMemberMapper;

    @GetMapping
    public String classroomGetHandle() {
        return "classroom/form";
    }

    @PostMapping
    public String classroomPostHandle(@SessionAttribute(required = false) Member logonMember,
                                      @ModelAttribute CreateClassroomRequest ccr) {
        if (logonMember == null) {
            return "redirect:/login";
        }


        String id = UUID.randomUUID().toString().replaceAll("-", "");
        String joinCode = UUID.randomUUID().toString().split("-")[0];

        Classroom classroom = new Classroom(id, logonMember.getId(), ccr.name(), ccr.description(), joinCode);
        classroomMapper.insertOne(classroom);

        return "redirect:/classroom/" + id;
    }

/*    @GetMapping("/join")
    public String classroomJoinGetHandle(@SessionAttribute(required = false) Member logonMember) {
        if (logonMember == null) {
            return "redirect:/login";
        }
        return "notUse-join";
    }*/

    @PostMapping("/join")
    public String classroomJoinPostHandle(@SessionAttribute Member logonMember,
                                          @RequestParam String joinCode) {
        Classroom found = classroomMapper.selectByJoinCode(joinCode);
        if (logonMember == null || found == null || found.getTeacherId().equals(logonMember.getId())) {
            return "redirect:/index";
        }

        Map<String, String> map = Map.of("studentId", logonMember.getId(), "classroomId", found.getId());
        ClassMember classMember = classMemberMapper.selectByStudentAndClassId(map);
        if (classMember == null) {
            classMember = new ClassMember(logonMember.getId(), found.getId());
            classMemberMapper.insertOne(classMember);
        }

        return "redirect:/classroom/" + found.getId();
    }


    @GetMapping("/{classroomId}")
    public String classroomMainHandle(@SessionAttribute(required = false) Member logonMember,
                                      @PathVariable String classroomId,
                                      Model model) {
        if (logonMember == null) {
            return "redirect:/login";
        }

        Classroom found = classroomMapper.selectById(classroomId);
        if (found == null) {
            return "redirect:/index";
        }
        model.addAttribute("classroom", found);

        return "classroom/main";
    }


    @PostMapping("/{classroomId}/notice")
    public String classroomNoticePostHandler(@SessionAttribute(required = false) Member logonMember,
                                             @PathVariable String classroomId,
                                             @ModelAttribute CreateNoticeRequest cnr) {
        if (logonMember == null) {
            return "redirect:/login";
        }

        for (MultipartFile file : cnr.attachment()){
            if (file.isEmpty()){
                continue;
            }
            long size = file.getSize(); //파일 사이즈
            String contentType = file.getContentType(); //파일 타입 (image/png, application/pdf...)
            String name = file.getName(); // html에서 넘어온 인풋 이름(attachment)
            String originName = file.getOriginalFilename(); // 파일 원래 이름
            // file.getInputStream();
            System.out.println("file size: " + file.getSize());
            System.out.println("contentType: " + contentType);
            System.out.println("name: " + name);
            System.out.println("originName: " + originName);
        }

        // 다음주부터 이 이후에 파일 받아서 아웃풋으로 넣는 작업 시작!

        return "redirect:/classroom/" + classroomId;
    }

}
