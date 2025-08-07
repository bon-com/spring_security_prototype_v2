package com.example.prototype.web.base.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 購入履歴クラス
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistoryDto {
    /** 購入日時 */
    private LocalDate purchaseDate;
    /** 購入商品 */
    private List<PurchaseItemDto> itemList;
}
