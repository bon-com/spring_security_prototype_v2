package com.example.prototype.web.base.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 検索履歴フォーム
 * 入力チェックを追加したいがためにフォームオブジェクト作成
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistoryConditionsForm {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    
}
