package com.example.prototype.web.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.prototype.biz.utils.MessageUtil;
import com.example.prototype.common.constants.Constants;

@Controller
public class SessionController {
    @Autowired
    private MessageUtil messageUtil;
    
    /**
     * 無効なセッション（例：サーバー再起動後に古いセッションIDでアクセス）
     * @param model
     * @return
     */
    @GetMapping(value = "/session-invalid")
    public String invalid(Model model) {
        model.addAttribute("message", messageUtil.getMessage(Constants.ERR_MSG_INVALID_SESSION));
        return "error";
    }
    
    /**
     * セッションが有効期限切れ（例：同一ユーザーの多重ログインで古いセッションが強制終了）
     * @param model
     * @return
     */
    @GetMapping(value = "/session-expired")
    public String expired(Model model) {
        model.addAttribute("message", messageUtil.getMessage(Constants.ERR_MSG_EXPIRED_SESSION));
        return "error";
    }   
}
