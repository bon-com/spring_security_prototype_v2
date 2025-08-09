package com.example.prototype.biz.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.prototype.biz.users.dao.JdbcUsersDao;
import com.example.prototype.biz.users.entity.ExtendedUser;
import com.example.prototype.common.constants.Constants;
import com.nimbusds.oauth2.sdk.util.StringUtils;

/**
 * 認証クラス
 */
@Service
public class AuthenticationUserService implements UserDetailsService {
    @Autowired
    private JdbcUsersDao jdbcUsersDao;

    /**
     * 認証用の情報を取得（Spring Securityのログイン処理にて内部で呼ばれる）
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // 入力値検証
        if (StringUtils.isBlank(loginId)) {
            throw new UsernameNotFoundException(Constants.ERR_MSG_AUTHENTICATION_BAD_CREDENTIALS);
        }
        
        // ログインID検索
        ExtendedUser user = jdbcUsersDao.findByLoginId(loginId);
        if (user == null) {
            throw new UsernameNotFoundException(Constants.ERR_MSG_AUTHENTICATION_BAD_CREDENTIALS);
        }
        
        return user;
    }

}
