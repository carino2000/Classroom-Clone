package com.example.app.classroom.controller;

import com.example.app.classroom.domain.Classroom;
import com.example.app.classroom.domain.Member;
import com.example.app.classroom.dto.projection.ClassroomWithTeacher;
import com.example.app.classroom.mapper.ClassroomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping("/index")
@RequiredArgsConstructor
public class IndexController {
    final ClassroomMapper classroomMapper;

    @GetMapping
    public String indexHandle(@SessionAttribute(required = false) Member logonMember,
                              Model model) {
        if(logonMember == null){
            return "index";
        }else if(!logonMember.isActive()){
            return "redirect:/verify/send/email";
        }else{
            List<ClassroomWithTeacher> classroomList = classroomMapper.selectAllMyClassroomById(logonMember.getId());
            model.addAttribute("classList", classroomList);
            model.addAttribute("classListSize", classroomList.size());
            return "index-logon";
        }
    }




}
