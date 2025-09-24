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
     * セッションタイムアウト、無効なセッションなど
     * @param model
     * @return
     */
    @GetMapping(value = "/session-invalid")
    public String invalid(Model model) {
        model.addAttribute("message", messageUtil.getMessage(Constants.ERR_MSG_INVALID_SESSION));
        return "error";
    }
    
}
