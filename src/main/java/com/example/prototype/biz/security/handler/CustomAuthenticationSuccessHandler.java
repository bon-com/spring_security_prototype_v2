package com.example.prototype.biz.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.prototype.biz.users.entity.ExtendedUser;
import com.example.prototype.common.constants.Constants;

/**
 * OAuth2連携成功後処理クラス
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler  {
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // OAuth2連携後のリダイレクト先トップページ
        String redirectUrl = "/spring_security_prototype_v2/";
        
        String uri = request.getRequestURI();
        if (uri.contains(Constants.GOOGLE_REGISTRATION_ID_LINK)) {
            ExtendedUser user = (ExtendedUser) authentication.getPrincipal();
            if (user.isGoogleLinked()) {
                // 紐づけ成功
                redirectUrl = redirectUrl + "?msgKey=" + Constants.GOOGLE_LINK_SUCCESS_KEY;
            } else {
                // 紐づけ失敗
                redirectUrl = redirectUrl + "?msgKey=" + Constants.GOOGLE_LINK_FAILURE_KEY;
            }
        } 

        response.sendRedirect(redirectUrl);
    }
}