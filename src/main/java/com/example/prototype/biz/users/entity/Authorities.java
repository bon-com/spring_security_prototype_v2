package com.example.prototype.biz.users.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 権限紐づきテーブルエンティティ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authorities {
    /** ログインID */
    private String loginId;
    /** 権限マスタID */
    private Integer authorityId;
}
