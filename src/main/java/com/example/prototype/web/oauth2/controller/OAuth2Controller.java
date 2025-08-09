package com.example.prototype.web.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.prototype.biz.users.entity.ExtendedUser;
import com.example.prototype.biz.users.service.UsersService;
import com.example.prototype.common.constants.Constants;

@Controller
@RequestMapping("oauth2")
public class OAuth2Controller {
    @Autowired
    private UsersService userService;
    
    /**
     * Google紐づけ解除
     * @return
     */
    @GetMapping(value = "/google/sub/cansel")
    public String canselGoogleSub() {
        // Googleアカウントとの紐づけ状態を解除
        var user = (ExtendedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setGoogleLinked(false);
        user.setGoogleSub("");
        userService.updateGoogleSub(user);
        
        return "redirect:/?msgKey=" + Constants.GOOGLE_LINK_CANSEL_KEY; 
    }
}
