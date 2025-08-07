package com.example.prototype.web.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.prototype.biz.users.service.UsersService;
import com.example.prototype.biz.utils.DataUtil;
import com.example.prototype.web.users.dto.UsersDto;

@Controller
@RequestMapping("admin")
public class AdminShowUserDetailController {
    @Autowired
    private UsersService userService;

    @GetMapping(value = "/users/{loginId}")
    public String detail(@PathVariable String loginId, Model model) {
        // 利用者情報取得
        UsersDto user = userService.findByLoginId(loginId);
        model.addAttribute("user", user);

        // 各日付を画面表示用にDate型にコンバート
        model.addAttribute("lastLoginAt", DataUtil.convertDateFromLocalDateTime(user.getLastLoginAt()));
        model.addAttribute("accountExpiryAt", DataUtil.convertDateFromLocalDateTime(user.getAccountExpiryAt()));
        model.addAttribute("passwordExpiryAt", DataUtil.convertDateFromLocalDateTime(user.getPasswordExpiryAt()));
        model.addAttribute("authorities", userService.findAuthorityByLoginId(loginId));

        return "admin/admin_user_detail";
    }
}
