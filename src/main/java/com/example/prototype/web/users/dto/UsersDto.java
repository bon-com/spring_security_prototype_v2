package com.example.prototype.web.users.dto;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.example.prototype.common.constants.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 利用者クラス
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {
    /** ログインID */
    private String loginId;
    /** 利用者氏名 */
    private String username;
    /** マスク済みパスワード */
    private String maskedPassword = Constants.MASKING_ITEM;
    /** アカウント有効可否（true:有効/false:無効） */
    private boolean enabled;
    /** アカウント有効期限切れ可否（true:有効/false:期限切れ） */
    private boolean accountNonExpired;
    /** パスワード有効期限切れ可否（true:有効/false:期限切れ） */
    private boolean credentialsNonExpired;
    /** アカウントロック可否（true:ロックなし/false:ロックあり） */
    private boolean accountNonLocked;
    /** 権限リスト */
    private Collection<? extends GrantedAuthority> authorities;
    /** ログイン失敗回数 */
    private int loginFailureCount;
    /** 最終ログイン日時 */
    private LocalDateTime lastLoginAt;
    /** アカウント有効期限日時 */
    private LocalDateTime accountExpiryAt;
    /** パスワード有効期限日時 */
    private LocalDateTime passwordExpiryAt;
}
