package com.example.prototype.biz.base.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.prototype.base.entity.PurchaseHistory;
import com.example.prototype.biz.base.dao.JdbcPurchaseHistoryDao;
import com.example.prototype.web.base.dto.PurchaseHistoryDto;
import com.example.prototype.web.base.dto.PurchaseItemDto;

/**
 * 注文履歴サービス
 */
@Service
public class PurchaseHistoryService {
    @Autowired
    private JdbcPurchaseHistoryDao jdbcPurchaseHistoryDao;

    /**
     * 購入日付一覧を取得
     * @return
     */
    public List<LocalDate> getPurchaseDateList() {
        return jdbcPurchaseHistoryDao.findAllPurchaseDate();
    }

    /**
     * 購入日付から履歴情報取得
     * @param purchaseDate
     * @return
     */
    public PurchaseHistoryDto findPurchaseHistory(LocalDate purchaseDate) {
        // 購入履歴情報取得
        PurchaseHistory purchaseHistory = jdbcPurchaseHistoryDao.findByPurchaseDate(purchaseDate);
        var purchaseHistoryDto = new PurchaseHistoryDto();
        purchaseHistoryDto.setItemList(new ArrayList<PurchaseItemDto>());
        BeanUtils.copyProperties(purchaseHistory, purchaseHistoryDto);
        
        purchaseHistory.getItemList().forEach(item -> {
            var dto = new PurchaseItemDto();
            BeanUtils.copyProperties(item, dto);
            purchaseHistoryDto.getItemList().add(dto);
        });
        
        return purchaseHistoryDto;
    }
    
    /**
     * 購入履歴合計金額を取得
     * @param purchaseList
     * @return
     */
    public int getTotalPrice(List<PurchaseItemDto> purchaseList) {
        return purchaseList.stream()
                .mapToInt(PurchaseItemDto::getPrice)
                .sum();
    }
}
