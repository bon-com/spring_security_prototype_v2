package com.example.prototype.web.base.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.prototype.biz.users.entity.ExtendedUser;
import com.example.prototype.common.constants.Constants;

@Controller
public class WelcomeController {
    /** パスワード有効期限切れ事前通知日の閾値 */
    @Value("${auth.login.password.expiry.warning.days}")
    private int passwordExpiryWarningDays;
    
    /**
     * TOP画面表示
     * @param model
     * @return
     */
    @GetMapping(value = "/")
    public String top(Model model) {
        // パスワード有効期限日時取得
        var authUser = (ExtendedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDateTime passwordExpiryAt = authUser.getPasswordExpiryAt();
        
        // パスワード有効期限前の警告メッセージチェック
        long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), passwordExpiryAt);
        if (daysLeft <= passwordExpiryWarningDays) {
            model.addAttribute("warning", String.format(Constants.MSG_PASSWORD_EXPIRY_TEMPLATE, daysLeft));
        }

        return "base/top";
    }
}
