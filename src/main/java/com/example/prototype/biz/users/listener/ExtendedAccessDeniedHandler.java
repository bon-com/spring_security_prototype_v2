package com.example.prototype.biz.users.listener;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.prototype.biz.users.entity.ExtendedUser;

/**
 * 認可エラー時のエラーをハンドリングするクラス
 * ※認可エラー時はイベントが発行されない
 */
@Component
public class ExtendedAccessDeniedHandler implements AccessDeniedHandler {
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(ExtendedAccessDeniedHandler.class);
    
    /**
     * 認可されていないロールでリクエストして403が返却された場合にハンドリング
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {
        
        // 不正アクセス者のログ
        var auth = (Authentication) request.getUserPrincipal();
        var authUser = (ExtendedUser) auth.getPrincipal();
        logger.warn("\n★★不正アクセス★★\n・ログインID: {}\n・アクセスパス: {}\n・理由: {}\n",
                authUser.getLoginId(), request.getRequestURI(), ex.getMessage());
        
        // エラー画面にリダイレクト
        response.sendRedirect(request.getContextPath() + "/system/error?code=403");
    }
}
