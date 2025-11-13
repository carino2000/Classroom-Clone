package com.example.app.classroom.interceptor;

import com.example.app.classroom.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

public class LogonInterceptor implements HandlerInterceptor {

    public void preHandle(HttpServletRequest req) throws Exception {

        String requestURI = req.getRequestURI();

        Member m = (Member)req.getSession().getAttribute("logonMember");
        if (m == null) {

        } else {

        }

    }

}
