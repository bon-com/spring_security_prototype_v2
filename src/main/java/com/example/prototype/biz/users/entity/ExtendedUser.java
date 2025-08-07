package com.example.prototype.biz.users.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.prototype.common.constants.Constants;

import lombok.Data;

/**
 * 利用者拡張エンティティ
 * Spring SecurityのUserを継承して拡張している
 */
@Data
public class ExtendedUser implements UserDetails {
    // ------------------------------
    // 既存フィールド ここから
    // ------------------------------
    /** ログインID */
    private String loginId;
    /** 利用者氏名 */
    private String username;
    /** パスワード */
    private String password;
    /** アカウント有効可否（true:有効/false:無効） */
    private boolean enabled;
    /** アカウント有効期限切れ可否（true:有効/false:期限切れ） */
    @SuppressWarnings("unused")
    private boolean accountNonExpired;
    /** パスワード有効期限切れ可否（true:有効/false:期限切れ） */
    @SuppressWarnings("unused")
    private boolean credentialsNonExpired;
    /** アカウントロック可否（true:ロックなし/false:ロックあり） */
    private boolean accountNonLocked;
    /** 権限リスト */
    private Collection<? extends GrantedAuthority> authorities;
    // ------------------------------
    // 既存フィールド ここまで
    // ------------------------------
    /** ログイン失敗回数 */
    private int loginFailureCount;
    /** 最終ログイン日時 */
    private LocalDateTime lastLoginAt;
    /** アカウント有効期限日時 */
    private LocalDateTime accountExpiryAt;
    /** パスワード有効期限日時 */
    private LocalDateTime passwordExpiryAt;

    public ExtendedUser(
            String loginId,
            String username,
            String password,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities,
            int loginFailureCount,
            LocalDateTime lastLoginAt,
            LocalDateTime accountExpiryAt,
            LocalDateTime passwordExpiryAt) {
        this.loginId = loginId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
        this.loginFailureCount = loginFailureCount;
        this.lastLoginAt = lastLoginAt;
        this.accountExpiryAt = accountExpiryAt;
        this.passwordExpiryAt = passwordExpiryAt;
    }

    @Override
    public boolean isAccountNonExpired() {
        /*
         * このメソッドをオーバーライドすることで、falseが返却された場合
         * Spring Securityが自動でAccountExpiredExceptionをスローして認証時にアカウント有効期限切れエラーになる
         */
        return LocalDateTime.now().isBefore(accountExpiryAt);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        /*
         * このメソッドをオーバーライドすることで、falseが返却された場合
         * Spring Securityが自動でCredentialsExpiredExceptionをスローして認証時にパスワード有効期限切れエラーになる
         */
        return LocalDateTime.now().isBefore(passwordExpiryAt);
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * ビルダークラス
     * 利用者拡張エンティティを作成する
     */
    @Data
    public static class Builder {
        /*
         * 以下のフィールドは必須となる
         * ・loginId
         * ・username
         * ・accountExpiryAt
         * ・passwordExpiryAt
         */
        private String loginId;
        private String username;
        private String password;
        private boolean enabled;
        private boolean accountNonExpired;
        private boolean credentialsNonExpired;
        private boolean accountNonLocked;
        private List<GrantedAuthority> authorities = new ArrayList<>();
        private int loginFailureCount;
        private LocalDateTime lastLoginAt;
        private LocalDateTime accountExpiryAt;
        private LocalDateTime passwordExpiryAt;

        public Builder loginId(String loginId) {
            this.loginId = loginId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder accountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public Builder credentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public Builder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public Builder loginFailureCount(int loginFailureCount) {
            this.loginFailureCount = loginFailureCount;
            return this;
        }

        public Builder lastLoginAt(LocalDateTime lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }

        public Builder accountExpiryAt(LocalDateTime accountExpiryAt) {
            this.accountExpiryAt = accountExpiryAt;
            return this;
        }

        public Builder passwordExpiryAt(LocalDateTime passwordExpiryAt) {
            this.passwordExpiryAt = passwordExpiryAt;
            return this;
        }

        public void addAuthority(GrantedAuthority authority) {
            this.authorities.add(authority);
        }

        public ExtendedUser build() {
            // 入力不足フィールド集計
            List<String> missingFields = new ArrayList<>();
            if (loginId == null) {
                missingFields.add("loginId");
            }
            if (username == null) {
                missingFields.add("username");
            }
            if (password == null) {
                missingFields.add("password");
            }
            if (accountExpiryAt == null) {
                missingFields.add("accountExpiryAt");
            }
            if (passwordExpiryAt == null) {
                missingFields.add("passwordExpiryAt");
            }

            if (!missingFields.isEmpty()) {
                // 必須項目チェックエラー
                throw new IllegalStateException(
                        Constants.ERR_MSG_DEFAULT + " 必須フィールド: " + String.join(", ", missingFields));
            }

            var now = LocalDateTime.now();
            // アカウント有効期限切れ判定
            boolean accountNonExpired = accountExpiryAt == null || accountExpiryAt.isAfter(now);
            // パスワード有効期限切れ判定
            boolean credentialsNonExpired = passwordExpiryAt == null || passwordExpiryAt.isAfter(now);

            return new ExtendedUser(
                    this.loginId,
                    this.username,
                    this.password,
                    this.enabled,
                    accountNonExpired,
                    credentialsNonExpired,
                    this.accountNonLocked,
                    this.authorities,
                    this.loginFailureCount,
                    this.lastLoginAt,
                    this.accountExpiryAt,
                    this.passwordExpiryAt);
        }
    }
    
    /*
     * toString時にパスワードをマスキング
     */
    @Override
    public String toString() {
        return "ExtendedUser{" +
                "loginId='" + loginId + '\'' +
                ", username='" + username + '\'' +
                ", password=" + Constants.MASKING_ITEM +
                ", enabled=" + enabled +
                ", accountNonLocked=" + accountNonLocked +
                ", loginFailureCount=" + loginFailureCount +
                ", lastLoginAt=" + lastLoginAt +
                ", accountExpiryAt=" + accountExpiryAt +
                ", passwordExpiryAt=" + passwordExpiryAt +
                ", authorities=" + authorities +
                '}';
    }
    // ------------------------------
    // 二重ログイン判定を行うため、オーバーライド　ここから
    // ------------------------------
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ExtendedUser that = (ExtendedUser) obj;
        return Objects.equals(this.getLoginId(), that.getLoginId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLoginId());
    }
    // ------------------------------
    // 二重ログイン判定を行うため、オーバーライド　ここまで
    // ------------------------------
}
