package com.example.prototype.web.users.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.prototype.biz.app.initializer.MasterLoader;
import com.example.prototype.biz.users.service.UsersService;
import com.example.prototype.biz.utils.DataUtil;
import com.example.prototype.common.constants.Constants;
import com.example.prototype.web.users.dto.AuthorityMasterDto;
import com.example.prototype.web.users.dto.UsersForm;

@Controller
@RequestMapping("admin")
@SessionAttributes(types = { UsersForm.class })
public class AdminCreateUserController {
    @Autowired
    private UsersService userService;
    
    @Autowired
    private MasterLoader masterLoader; 
    
    @ModelAttribute
    public UsersForm setUpUsersForm() {
        return new UsersForm();
    }
    
    @ModelAttribute("authorityList")
    public List<AuthorityMasterDto> setUpAuthorityList() {
        return masterLoader.getCachedAuthorityList();
    }

    /**
     * 利用者新規登録画面表示
     * @param model
     * @return
     */
    @GetMapping(value = "/users/register")
    public String register(Model model) {
        return "admin/admin_create_user";
    }
    
    /**
     * 利用者新規登録
     * @param form
     * @param rs
     * @param model
     * @return
     */
    @PostMapping(value = "/users/register")
    public String registerReq(@Valid UsersForm form, BindingResult rs, Model model) {
        if (rs.hasErrors()) {
            return "admin/admin_create_user";
        }
        
        // ログインID重複確認
        int count = userService.findCountByLoginId(form.getLoginId());
        if (count != 0) {
            model.addAttribute("warning", Constants.ERR_MSG_LOGIN_ID_DUPLICATE);
            return "admin/admin_create_user";
        }
        
        // 確認画面に遷移
        return "redirect:/admin/users/register/confirm";
    }
    
    /**
     * 利用者新規登録確認画面表示
     * @param form
     * @param model
     * @return
     */
    @GetMapping(value = "/users/register/confirm")
    public String confirm(UsersForm form, Model model) {
        model.addAttribute("loginId", form.getLoginId());
        model.addAttribute("username", form.getUsername());
        model.addAttribute("enabled", form.isEnabled());
        model.addAttribute("accountExpiryAt", DataUtil.convertDateFromLocalDateTime(form.getAccountExpiryAt()));
        model.addAttribute("passwordExpiryAt", DataUtil.convertDateFromLocalDateTime(form.getPasswordExpiryAt()));
        model.addAttribute("accountNonLocked", form.isAccountNonLocked());
        model.addAttribute("authorities", userService.getAuthority(form.getAuthorityIds()));
        
        return "admin/admin_create_user_confirm";
    }
    
    /**
     * 新規登録処理間完了
     * @param form
     * @param sessionStatus
     * @return
     */
    @GetMapping(value = "/users/register/complete")
    public String complate(UsersForm form, SessionStatus sessionStatus) {
        userService.insertUser(form);
        // @SessionAttributesで指定した属性の削除
        sessionStatus.setComplete(); 
        return "redirect:/admin/users?msgKey=" + Constants.INSERT_SUCCESS_KEY;
    }
}
