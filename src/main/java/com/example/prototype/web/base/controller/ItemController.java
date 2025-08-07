package com.example.prototype.web.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.prototype.biz.base.service.ItemService;

@Controller
@RequestMapping("items")
public class ItemController {
    /** 商品サービス */
    @Autowired
    private ItemService itemService;

    /**
     * 商品一覧表示
     * @param model
     * @return
     */
    @GetMapping(value = "/")
    public String items(Model model) {
        // 商品一覧を取得
        model.addAttribute("items", itemService.findAll());

        return "base/items";
    }
}
