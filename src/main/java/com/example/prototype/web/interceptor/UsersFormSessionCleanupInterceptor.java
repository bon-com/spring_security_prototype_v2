package com.example.prototype.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class UsersFormSessionCleanupInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // セッションが管理しているuserFormを削除
        request.getSession().removeAttribute("usersForm");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
