package com.example.prototype.web.users.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 認証情報ロギングフィルター
 */
public class AuthLoggingFilter implements Filter {
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(AuthLoggingFilter.class);

    /**
     * 認証情報をログ出力する
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            logger.debug("\n★★認証情報★★\nログインユーザー: {}\nユーザー権限： {}\n", auth.getName(), auth.getAuthorities());
        }

        chain.doFilter(request, response);
    }

}