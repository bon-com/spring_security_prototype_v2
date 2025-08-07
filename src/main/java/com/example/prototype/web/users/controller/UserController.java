package com.example.prototype.web.users.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.prototype.biz.users.entity.ExtendedUser;
import com.example.prototype.biz.users.service.UsersService;
import com.example.prototype.common.constants.Constants;
import com.example.prototype.web.users.dto.UserPasswordForm;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UsersService userService;
    
    @ModelAttribute
    public UserPasswordForm setUp() {
        return new UserPasswordForm();
    }
    
    /**
     * パスワード更新画面表示
     * @return
     */
    @GetMapping(value = "/password-update")
    public String password(@RequestParam(name = "msgKey", required = false) String msgKey, Model model) {
        if (Constants.UPDATE_SUCCESS_KEY.equals(msgKey)) {
            model.addAttribute("message", Constants.MSG_UPDATE_SUCCESS);
        }
        
        return "user/user_update_password";
    }
    
    /**
     * パスワード更新
     * @param form
     * @param rs
     * @return
     */
    @PostMapping(value = "/password-update")
    public String passwordUpdate(@Valid UserPasswordForm form, BindingResult rs) {
        if (rs.hasErrors()) {
            return "user/user_update_password";
        }
        
        // パスワード更新
        var authUser = (ExtendedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authUser.setPassword(form.getNewPassword());
        userService.updatePassword(authUser);
        
        return "redirect:/user/password-update?msgKey=" + Constants.UPDATE_SUCCESS_KEY;
    }
}
