package com.example.prototype.web.users.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.prototype.biz.app.initializer.MasterLoader;
import com.example.prototype.biz.users.service.UsersService;
import com.example.prototype.biz.utils.DataUtil;
import com.example.prototype.common.constants.Constants;
import com.example.prototype.web.users.dto.AuthorityMasterDto;
import com.example.prototype.web.users.dto.UsersDto;
import com.example.prototype.web.users.dto.UsersForm;

@Controller
@RequestMapping("admin")
@SessionAttributes(types = { UsersForm.class })
public class AdminEditUserController {
    @Autowired
    private UsersService userService;
    
    @Autowired
    private MasterLoader masterLoader; 
    
    @ModelAttribute("authorityList")
    public List<AuthorityMasterDto> setUpAuthorityList() {
        return masterLoader.getCachedAuthorityList();
    }
    
    @ModelAttribute
    public UsersForm setUpUsersForm() {
        return new UsersForm();
    }
    
    /**
     * 利用者情報の編集画面表示
     * @param loginId
     * @param form
     * @param model
     * @return
     */
    @GetMapping(value = "/users/edit/{loginId}")
    public String edit(@PathVariable String loginId, @RequestParam(required = false) String back, UsersForm form, Model model) {
        if (back == null) {
            // 利用者情報取得
            UsersDto user = userService.findByLoginId(loginId);
            BeanUtils.copyProperties(user, form);
            
            // 利用者権限情報編集
            List<AuthorityMasterDto> authorities = userService.findAuthorityByLoginId(loginId);
            List<Integer> authorityIds = UsersViewHelper.convertAuthorities(authorities);
            form.setAuthorityIds(authorityIds);
        }
        
        return "admin/admin_edit_user";
    }
    
    /**
     * 利用者情報編集
     * @param form
     * @param rs
     * @return
     */
    @PostMapping(value = "/users/edit/{loginId}")
    public String editReq(@Valid UsersForm form, BindingResult rs) {
        if (rs.hasErrors()) {
            return "admin/admin_edit_user";
        }
        
        // 確認画面に遷移
        return "redirect:/admin/users/edit/confirm";
    }
    
    /**
     * 確認画面表示
     * @param form
     * @param model
     * @return
     */
    @GetMapping(value = "/users/edit/confirm")
    public String confirm(UsersForm form, Model model) {
        model.addAttribute("loginId", form.getLoginId());
        model.addAttribute("username", form.getUsername());
        model.addAttribute("enabled", form.isEnabled());
        model.addAttribute("accountExpiryAt", DataUtil.convertDateFromLocalDateTime(form.getAccountExpiryAt()));
        model.addAttribute("passwordExpiryAt", DataUtil.convertDateFromLocalDateTime(form.getPasswordExpiryAt()));
        model.addAttribute("accountNonLocked", form.isAccountNonLocked());
        model.addAttribute("authorities", userService.getAuthority(form.getAuthorityIds()));
        
        return "admin/admin_edit_user_confirm";
    }
    
    /**
     * 利用者情報の更新処理完了
     * @param form
     * @param sessionStatus
     * @return
     */
    @GetMapping(value = "/users/edit/complete")
    public String complate(UsersForm form, SessionStatus sessionStatus) {
        userService.updateUser(form);
        // @SessionAttributesで指定した属性の削除
        sessionStatus.setComplete(); 
        return "redirect:/admin/users?msgKey=" + Constants.UPDATE_SUCCESS_KEY;
    }
}
