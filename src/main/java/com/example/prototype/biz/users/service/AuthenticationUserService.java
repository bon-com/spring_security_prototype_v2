package com.example.prototype.biz.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.prototype.biz.users.dao.JdbcUsersDao;

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
        // ログインID検索
        return jdbcUsersDao.findByLoginId(loginId);
    }

}
