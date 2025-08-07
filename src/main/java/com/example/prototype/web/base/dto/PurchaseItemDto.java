package com.example.prototype.web.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 購入商品履歴クラス
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItemDto {
    /** 商品ID（外部キー） */
    private int itemId;
    /** 購入数量 */
    private int quantity;
    /** 購入時の値段 */
    private int price;
    /** 商品名 */
    private String itemName;
}
