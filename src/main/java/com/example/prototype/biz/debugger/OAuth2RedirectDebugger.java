package com.example.prototype.biz.debugger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * OAuth2リダイレクト時のデバッグ用
 */
@Component
public class OAuth2RedirectDebugger extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(OAuth2RedirectDebugger.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.contains("/login/oauth2/code")) {
            logger.debug("\n★★[DEBUG] OAuth2 Redirect URI accessed★★: " + uri);
            
            // パラメータ確認
            Map<String, String[]> paramMap = request.getParameterMap();
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                logger.debug("\n★★[DEBUG] Param★★: " + key + " = " + Arrays.toString(values));
            }
        }

        filterChain.doFilter(request, response);
    }

}

