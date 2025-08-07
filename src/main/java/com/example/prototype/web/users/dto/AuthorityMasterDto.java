package com.example.prototype.web.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 権限マスタクラス
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityMasterDto {
    /** 権限マスタID */
    private int authorityId;
    /** 権限表示名 */
    private String authorityName;
}
