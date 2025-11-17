package com.example.app.classroom.controller;

import com.example.app.classroom.domain.*;
import com.example.app.classroom.dto.projection.NoticeWithAttachment;
import com.example.app.classroom.dto.request.CreateClassroomRequest;
import com.example.app.classroom.dto.request.CreateNoticeRequest;
import com.example.app.classroom.mapper.ClassMemberMapper;
import com.example.app.classroom.mapper.ClassroomMapper;
import com.example.app.classroom.mapper.MemberMapper;
import com.example.app.classroom.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/classroom")
public class ClassroomController {
    final ClassroomMapper classroomMapper;
    final ClassMemberMapper classMemberMapper;
    final NoticeMapper noticeMapper;
    final MemberMapper memberMapper;


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

        List<Notice> noticeList = noticeMapper.selectNoticeByClassroomId(found.getId());

        List<NoticeWithAttachment> noticeWithAttachments = new ArrayList<>();
        for(Notice one : noticeList){
            NoticeWithAttachment nwa = new NoticeWithAttachment();
            nwa.setContent(one.getContent());
            nwa.setCreatedAt(one.getCreatedAt());
            nwa.setId(one.getId());
            nwa.setClassroomId(one.getClassroomId());
            nwa.setAttachments(noticeMapper.selectAttachmentByNoticeId(one.getId()));

            noticeWithAttachments.add(nwa);
        }

        model.addAttribute("classroom", found);
        model.addAttribute("noticeList", noticeWithAttachments);

        return "classroom/main";
    }

    @GetMapping("/{classroomId}/attendance")
    public String classroomAttendanceGetHandle(@SessionAttribute(required = false) Member logonMember,
                                               @PathVariable String classroomId,
                                               Model model) {
        if (logonMember == null) {
            return "redirect:/login";
        }
        Classroom found = classroomMapper.selectById(classroomId);
        if (found == null) {
            return "redirect:/index";
        }

        Member teacher = memberMapper.selectById(found.getTeacherId());
        List<String> ids = classMemberMapper.selectStudentIdByClassroomId(found.getId());
        List<Member> students = new ArrayList<>();
        if(!ids.isEmpty()){
            students = memberMapper.selectByIds(ids);
        }else{
            students = null;
        }

        model.addAttribute("classroom", found);
        model.addAttribute("teacher", teacher);
        model.addAttribute("students", students);

        return "classroom/attendance";
    }


    @PostMapping("/{classroomId}/notice")
    public String classroomNoticePostHandler(@SessionAttribute(required = false) Member logonMember,
                                             @PathVariable String classroomId,
                                             @ModelAttribute CreateNoticeRequest cnr) {
        if (logonMember == null) {
            return "redirect:/login";
        }
        if(cnr.content().isEmpty() && cnr.attachment().getFirst().isEmpty()) { //컨텐츠도, 첨부파일도 없으면 인서트 안하고 리다이렉트
            return "redirect:/classroom/" + classroomId;
        }

        Notice notice = new  Notice(classroomId, cnr.content());
        noticeMapper.insertNotice(notice); //매퍼 쿼리에서 id 반환 (useGeneratedKeys=true)


        try{
            for (MultipartFile file : cnr.attachment()) {
                if (file.isEmpty()) {
                    continue;
                }

                /*
                long size = file.getSize(); //파일 사이즈
                String contentType = file.getContentType(); //파일 타입 (image/png, application/pdf...)
                String name = file.getName(); // html에서 넘어온 인풋 이름(attachment)
                String originName = file.getOriginalFilename(); // 파일 원래 이름
                 */

                String uuid = UUID.randomUUID().toString().replaceAll("-", ""); // 파일 이름 겹치기 방지용 폴더 이름 생성
                Path uploadPath = Path.of(System.getProperty("user.home"), "files", uuid);
                Files.createDirectories(uploadPath);
                file.transferTo(Path.of(uploadPath.toString(), file.getOriginalFilename()));

                NoticeAttachment attachment = new NoticeAttachment();
                attachment.setNoticeId(notice.getId());
                attachment.setFileName(file.getOriginalFilename());
                attachment.setFileSize(file.getSize());
                attachment.setFileContentType(file.getContentType());
                attachment.setFileUrl("/files/" + uuid +  "/" + file.getOriginalFilename());
                noticeMapper.insertAttachment(attachment);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/classroom/" + classroomId;
    }

}
