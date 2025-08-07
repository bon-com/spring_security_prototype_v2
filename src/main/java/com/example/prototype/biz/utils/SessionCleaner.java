package com.example.prototype.biz.utils;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionCleaner {
    /**
     * セッションをクリアする
     * @param session
     */
    public void clearSession(HttpSession session) {
        // 認証情報クリア
        SecurityContextHolder.clearContext();
        
        if (session != null) {
            // セッション削除
            session.invalidate();
        }
    }
}
