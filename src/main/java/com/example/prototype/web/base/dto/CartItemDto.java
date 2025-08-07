package com.example.prototype.web.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 商品と数量を保持するクラス
 */
@Data
@AllArgsConstructor
public class CartItemDto {
    /** 商品 */
    private ItemDto item;
    /** 数量 */
    private int quantity;

    /** 合計金額を返却 */
    public int getTotal() {
        return item.getPrice() * quantity;
    }
}
