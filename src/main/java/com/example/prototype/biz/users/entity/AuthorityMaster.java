package com.example.prototype.biz.users.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 権限マスタエンティティ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityMaster {
    /** 権限マスタID */
    private int authorityId;
    /** 権限コード */
    private String authorityCode;
    /** 権限表示名 */
    private String authorityName;
    /** 表示順 */
    private int displayOrder;
    /** 権限マスタ有効可否（true:有効/false:無効） */
    private boolean isActive;
}
