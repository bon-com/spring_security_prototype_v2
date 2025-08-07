package com.example.prototype.biz.users.listener;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.example.prototype.biz.users.entity.ExtendedUser;
import com.example.prototype.biz.users.service.UsersService;
import com.example.prototype.common.constants.Constants;

/**
 * 認証成功時のイベントをハンドリングするクラス
 */
@Component
public class LoginSuccessEventListener {
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(LoginSuccessEventListener.class);

    @Autowired
    private UsersService usersService;

    /**
     * 認証成功後に呼ばれるイベントリスナーをハンドリング
     * @param event
     */
    @EventListener
    public void handleLoginSuccess(InteractiveAuthenticationSuccessEvent event) {
        // ログイン利用者存在確認 （※認証成功した場合、getPrincipal()にてUserDetailsオブジェクトを取得する）
        var authUser = (ExtendedUser) event.getAuthentication().getPrincipal();
        ExtendedUser user = usersService.findByLoginIdForAuth(authUser.getLoginId());
        if (user == null) {
            logger.warn("\n★★認証ユーザー取得失敗★★\n認証成功後にユーザー情報が見つかりません: {}\n", authUser.getLoginId());
            throw new IllegalStateException("\nユーザー情報が存在しません: {}\n" + authUser.getLoginId());
        }

        // 認証情報の更新
        user.setLoginFailureCount(0);
        user.setLastLoginAt(LocalDateTime.now());
        usersService.updateAuthStatus(user);

        // 認証成功ログ
        logger.info("\n★★認証成功★★\n・利用者： {}\n・ログイン失敗回数: {}\n・アカウントロック状態: {}\n", user.getLoginId(),
                user.getLoginFailureCount(), user.isAccountNonLocked() ? Constants.ACCOUNT_NOT_LOCKED : Constants.ACCOUNT_LOCKED);
    }
}
