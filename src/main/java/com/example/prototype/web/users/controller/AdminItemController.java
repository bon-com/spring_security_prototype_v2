package com.example.prototype.web.users.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.example.prototype.biz.base.service.ItemService;
import com.example.prototype.common.constants.Constants;
import com.example.prototype.web.base.dto.ItemDto;
import com.example.prototype.web.base.dto.ItemForm;

@Controller
@RequestMapping("admin")
public class AdminItemController {
    @Autowired
    private ItemService itemService;

    @ModelAttribute
    public ItemForm setUpItemForm() {
        return new ItemForm();
    }

    @ModelAttribute("items")
    public List<ItemDto> setUpItems() {
        return itemService.findAllByAdmin();
    }

    /**
     * 商品一覧画面表示
     * @param model
     * @return
     */
    @GetMapping(value = "/items")
    public String items(@RequestParam(name = "msgKey", required = false) String msgKey, Model model) {
        if (Constants.UPDATE_SUCCESS_KEY.equals(msgKey)) {
            // 更新メッセージ制御
            model.addAttribute("message", Constants.MSG_UPDATE_SUCCESS);
        } else if (Constants.INSERT_SUCCESS_KEY.equals(msgKey)) {
            // 登録メッセージ制御
            model.addAttribute("message", Constants.MSG_INSERT_SUCCESS);
        }

        return "admin/admin_items";
    }

    /**
     * 商品登録
     * @param form
     * @param rs
     * @return
     */
    @PostMapping(value = "/items/register")
    public String insert(@Valid ItemForm form, BindingResult rs) {
        if (rs.hasErrors()) {
            return "admin/admin_items";
        }
        
        // 商品登録
        itemService.insert(form);
        return "redirect:/admin/items?msgKey=" + Constants.INSERT_SUCCESS_KEY;
    }

    /**
     * 商品を更新
     * @param id
     * @return
     */
    @GetMapping(value = "/items/update-deleted/{id}")
    public String updateDeleted(@PathVariable int id, @RequestParam boolean deleted, Model model) {
        // 商品の削除フラグを更新
        itemService.updateDeletedByAdmin(id, deleted);
        return "redirect:/admin/items?msgKey=" + Constants.UPDATE_SUCCESS_KEY;
    }
}
