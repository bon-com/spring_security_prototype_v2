package com.example.prototype.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品エンティティ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    /** 商品ID */
    private int id;
    /** 商品名 */
    private String name;
    /** 値段 */
    private int price;
    /** 削除フラグ */
    private boolean deleted;
}
