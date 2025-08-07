package com.example.prototype.base.entity;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 購入履歴エンティティ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistory {
    /** 購入履歴ID */
    private int id;
    /** 購入日時 */
    private LocalDate purchaseDate;
    /** 購入商品 */
    private List<PurchaseItem> itemList;

}
