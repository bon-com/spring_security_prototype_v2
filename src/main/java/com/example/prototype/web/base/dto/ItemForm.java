package com.example.prototype.web.base.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品フォーム
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemForm {
    @NotNull
    @Size(min =1, max = 100)
    /** 商品名 */
    private String name;
    /** 値段 */
    @NotNull
    private Integer price;
    /** 論理削除フラグ */
    private boolean deleted;
    
}
