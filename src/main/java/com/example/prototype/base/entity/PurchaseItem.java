package com.example.prototype.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 購入商品履歴エンティティ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItem {
    /** 購入商品ID */
    private int id;
    /** 購入履歴ID（外部キー） */
    private int purchaseId;
    /** 商品ID（外部キー） */
    private int itemId;
    /** 商品名 */
    private String itemName;
    /** 購入数量 */
    private int quantity;
    /** 購入時の値段 */
    private int price;
    /** 購入時の合計金額 */
    private int totalPrice;
}
