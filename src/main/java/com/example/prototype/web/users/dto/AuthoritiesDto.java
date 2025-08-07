package com.example.prototype.web.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 権限紐づきクラス
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthoritiesDto {
    /** ログインID */
    private String loginId;
    /** 権限マスタID */
    private Integer authorityId;
}
