package com.example.prototype.web.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.prototype.biz.users.service.UsersService;
import com.example.prototype.common.constants.Constants;

@Controller
@RequestMapping("admin")
public class AdminShowUserController {
    @Autowired
    private UsersService userService;
    
    /**
     * 利用者情報の一覧画面表示
     * @param model
     * @return
     */
    @GetMapping(value = "/users")
    public String users(@RequestParam(name = "msgKey", required = false) String msgKey, Model model) {
        if (Constants.UPDATE_SUCCESS_KEY.equals(msgKey)) {
            // 更新メッセージ制御
            model.addAttribute("message", Constants.MSG_UPDATE_SUCCESS);
        } else if (Constants.INSERT_SUCCESS_KEY.equals(msgKey)) {
            // 登録メッセージ制御
            model.addAttribute("message", Constants.MSG_INSERT_SUCCESS);
        }
        
        model.addAttribute("userList", userService.findAll());
        return "admin/admin_users_overview";
    }
}
