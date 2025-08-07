package com.example.prototype.web.base.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.prototype.biz.base.service.PurchaseHistoryService;
import com.example.prototype.web.base.dto.PurchaseHistoryConditionsForm;
import com.example.prototype.web.base.dto.PurchaseHistoryDto;

@Controller
@RequestMapping("history")
public class PurchaseHistoryController {
    @Autowired
    private PurchaseHistoryService purchaseHistoryService;
    
    /**
     * 購入日付一覧をモデルに追加
     * @return
     */
    @ModelAttribute("availableDates")
    public List<LocalDate> setAvailableDates() {
        return purchaseHistoryService.getPurchaseDateList();
    }
    
    /**
     * 購入履歴検索フォームをモデルに追加
     * @return
     */
    @ModelAttribute("historyForm")
    public PurchaseHistoryConditionsForm setPurchaseHistoryConditionsForm() {
        return new PurchaseHistoryConditionsForm();
    }
    
    /**
     * 初期表示
     * @param model
     * @return
     */
    @GetMapping(value = "/")
    public String historyDate(Model model) {
        return "base/purchase_history";
    }
    
    /**
     * 購入履歴検索
     * @param purchaseDateStr
     * @param model
     * @return
     */
    @GetMapping(value = "purchase-history")
    public String history(@Valid @ModelAttribute("historyForm") PurchaseHistoryConditionsForm form, BindingResult rs, Model model) {
        if (!rs.hasErrors()) {
            // 購入日付検索
            PurchaseHistoryDto purchaseHistory = purchaseHistoryService.findPurchaseHistory(form.getPurchaseDate());
            model.addAttribute("purchaseHistory", purchaseHistory);
            
            // 購入商品履歴から合計金額取得
            int totalPrice = purchaseHistoryService.getTotalPrice(purchaseHistory.getItemList());
            model.addAttribute("totalPrice", totalPrice);
        }
        return "base/purchase_history";
    }
}
