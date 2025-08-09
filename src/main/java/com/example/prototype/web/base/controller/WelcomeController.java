package com.example.prototype.web.base.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.prototype.biz.users.entity.ExtendedUser;
import com.example.prototype.biz.users.service.UsersService;
import com.example.prototype.common.constants.Constants;

@Controller
public class WelcomeController {
    @Autowired
    private UsersService userService;
    
    /** パスワード有効期限切れ事前通知日の閾値 */
    @Value("${auth.login.password.expiry.warning.days}")
    private int passwordExpiryWarningDays;
    
    @ModelAttribute("googleLinked")
    public boolean setUpGoogleLinked() {
        var user = (ExtendedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findGoogleLinkedByLoginId(user.getLoginId()); 
    }
    
    /**
     * TOP画面表示
     * @param model
     * @return
     */
    @GetMapping(value = "/")
    public String top(@RequestParam(name = "msgKey", required = false) String msgKey, Model model) {
        // OAuth連携メッセージ設定
        setOAuthMessage(msgKey, model);
        
        // パスワード有効期限日時取得
        var user = (ExtendedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDateTime passwordExpiryAt = user.getPasswordExpiryAt();
        
        // パスワード有効期限前の警告メッセージチェック
        long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), passwordExpiryAt);
        if (daysLeft <= passwordExpiryWarningDays) {
            model.addAttribute("warning", String.format(Constants.MSG_PASSWORD_EXPIRY_TEMPLATE, daysLeft));
        }

        return "base/top";
    }
    
    /**
     * OAuthメッセージの設定
     * @param msgKey
     * @param model
     */
    private void setOAuthMessage(String msgKey, Model model) {
        if (msgKey == null) {
            return;
        }
        
        switch(msgKey) {
        case Constants.GOOGLE_LINK_FAILURE_KEY:
            model.addAttribute("googleLinkWarning", Constants.ERR_MSG_UPDATE_GOOGLE_FAILURE);
            break;
        case Constants.GOOGLE_LINK_SUCCESS_KEY:
            model.addAttribute("message", Constants.MSG_UPDATE_GOOGLE_SUB_SUCCESS);
            break;
        case Constants.GOOGLE_LINK_CANSEL_KEY:
            model.addAttribute("message", Constants.MSG_CANSEL_GOOGLE_SUB);
            break;
        default:
            // 処理無し
        }
    }
}
