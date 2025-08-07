package com.example.prototype.web.base.dto;

import lombok.Data;

/**
 * カート追加リクエストフォーム
 */
@Data
public class CartAddForm {
    /** 商品ID */
    private int itemId;
    /** 数量 */
    private int quantity;
}
