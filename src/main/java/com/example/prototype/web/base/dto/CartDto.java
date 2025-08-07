package com.example.prototype.web.base.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;

/**
 * カートクラス
 */
@Data
public class CartDto {
    /** 商品毎の情報マップ */
    private Map<Integer, CartItemDto> items = new LinkedHashMap<>();

    /** カート内の数量を取得 */
    public int getTotal() {
        return items.values().stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();
    }
}