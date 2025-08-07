package com.example.prototype.biz.users.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;

import com.example.prototype.biz.users.entity.ExtendedUser;
import com.example.prototype.biz.users.service.UsersService;
import com.example.prototype.common.constants.Constants;

/**
 * 認証失敗時のイベントをハンドリングするクラス
 */
@Component
public class LoginFailureEventListener {
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(LoginFailureEventListener.class);

    @Autowired
    private UsersService usersService;
    
    /** ログイン失敗回数の閾値 */
    @Value("${auth.login.max.failure.count}")
    private int maxFailureCount;

    /**
     * 認証失敗後に呼ばれるイベントリスナー（※すべての失敗イベントを取得）をハンドリング
     * @param event
     */
    @EventListener
    public void handleLoginFailure(AbstractAuthenticationFailureEvent event) {
        // 利用者検索
        var auth = event.getAuthentication();
        String loginId = auth.getName(); // 認証失敗したusernameにあたる値
        ExtendedUser user = usersService.findByLoginIdForAuth(loginId);

        // 利用者情報が存在する場合、認証情報を更新
        if (user != null) {
            // ログイン失敗回数取得
            int failureCount = user.getLoginFailureCount();
            if (failureCount + 1 > maxFailureCount) {
                // アカウントロック
                user.setAccountNonLocked(false);
            } else {
                // ログイン失敗回数カウント
                user.setLoginFailureCount(failureCount + 1);
            }
            usersService.updateAuthStatus(user);
        }

        // 認証失敗ログ
        Exception ex = event.getException();
        String clientInfo = auth.getDetails().toString();
        String failureType = ex.getClass().getSimpleName();
        String message = ex.getMessage();
        logger.info("\n★★認証失敗★★:\nログインID: {}\n・失敗理由: {} ({})\n・詳細: {}\n・ログイン失敗回数: {}\n・アカウントロック状態: {}\n", loginId, failureType,
                message, clientInfo, user.getLoginFailureCount(), user.isAccountNonLocked() ? Constants.ACCOUNT_NOT_LOCKED : Constants.ACCOUNT_LOCKED);
    }

}
