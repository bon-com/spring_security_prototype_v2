package com.example.prototype.biz.users.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.prototype.biz.app.initializer.MasterLoader;
import com.example.prototype.biz.users.dao.JdbcAuthoritiesDao;
import com.example.prototype.biz.users.dao.JdbcUsersDao;
import com.example.prototype.biz.users.entity.Authorities;
import com.example.prototype.biz.users.entity.ExtendedUser;
import com.example.prototype.web.users.dto.AuthoritiesDto;
import com.example.prototype.web.users.dto.AuthorityMasterDto;
import com.example.prototype.web.users.dto.UsersDto;
import com.example.prototype.web.users.dto.UsersForm;

@Service
public class UsersService {
    @Autowired
    private JdbcUsersDao jdbcUsersDao;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JdbcAuthoritiesDao jdbcAuthoritiesDao;
    
    @Autowired
    private AuthoritiesService authoritiesService;
    
    @Autowired
    private MasterLoader masterLoader;

    /** パスワード有効期間 */
    @Value("${auth.password.expiry.period.days}")
    private int passwordExpiryPeriodDays;

    /**
     * 認証情報更新
     * @param user
     */
    public void updateAuthStatus(ExtendedUser user) {
        jdbcUsersDao.updateAuthStatus(user);
    }

    /**
     * ログインID検索（認証用）
     * @param username
     * @return
     */
    public ExtendedUser findByLoginIdForAuth(String loginId) {
        return jdbcUsersDao.findByLoginId(loginId);
    }

    /**
     * 認証情報更新
     * @param user
     */
    public void updatePassword(ExtendedUser user) {
        // パスワードのハッシュ化
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // パスワード有効期間更新
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(passwordExpiryPeriodDays);
        user.setPasswordExpiryAt(expiryDate);

        jdbcUsersDao.updatePassword(user);
    }

    /**
     * 利用者一覧取得
     * @return
     */
    public List<UsersDto> findAll() {
        List<UsersDto> userList = new ArrayList<>();

        jdbcUsersDao.findAll().forEach(exUser -> {
            var dto = new UsersDto();
            BeanUtils.copyProperties(exUser, dto);
            userList.add(dto);
        });
        
        return userList;
    }
    
    /**
     * 利用者情報の登録
     * @param form
     */
    public void insertUser(UsersForm form) {
        // 登録エンティティ作成
        var user = ExtendedUser.builder()
                .loginId(form.getLoginId())
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))
                .enabled(form.isEnabled())
                .accountNonLocked(form.isAccountNonLocked())
                .accountExpiryAt(form.getAccountExpiryAt())
                .passwordExpiryAt(form.getPasswordExpiryAt())
                .build();
        
        // 利用者登録
        jdbcUsersDao.insert(user);
        // 権限登録
        form.getAuthorityIds().forEach(authorityId -> {
            var authority = new Authorities(form.getLoginId(), authorityId);
            jdbcAuthoritiesDao.insert(authority);
        });
    }

    /**
     * ログインID検索
     * @param username
     * @return
     */
    public UsersDto findByLoginId(String loginId) {
        ExtendedUser user = jdbcUsersDao.findByLoginId(loginId);
        
        var dto = new UsersDto();
        BeanUtils.copyProperties(user, dto);
        
        return dto;
    }

    /**
     * 利用者の権限一覧取得
     * @param loginId
     * @return
     */
    public List<AuthorityMasterDto> findAuthorityByLoginId(String loginId) {
        // 権限マスタ一覧取得
        List<AuthorityMasterDto> authorityMasterList = masterLoader.getCachedAuthorityList();
        // 利用者の権限取得
        List<AuthoritiesDto> authoritiesList = authoritiesService.findByLoginId(loginId);
        
        return authorityMasterList.stream()
                .filter(master -> authoritiesList.stream()
                        .anyMatch(dto -> dto.getAuthorityId() == master.getAuthorityId()))
                    .collect(Collectors.toList());
    }
    
    /**
     * ログインID検索
     * @param loginId
     * @return
     */
    public int findCountByLoginId(String loginId) {
        return jdbcUsersDao.findCountByLoginId(loginId);
    }

    /**
     * 利用者の権限一覧取得
     * @param loginId
     * @return
     */
    public List<AuthorityMasterDto> getAuthority(List<Integer> authorityIds) {
        // 権限マスタ一覧取得
        List<AuthorityMasterDto> authorityMasterList = masterLoader.getCachedAuthorityList();
        
        return authorityMasterList.stream()
                .filter(master -> authorityIds.stream()
                        .anyMatch(i -> i == master.getAuthorityId()))
                    .collect(Collectors.toList());
    }
    
    /**
     * 利用者情報の更新
     * @param form
     */
    public void updateUser(UsersForm form) {
        // 更新エンティティ作成
        var user = ExtendedUser.builder()
                .loginId(form.getLoginId())
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))
                .enabled(form.isEnabled())
                .accountNonLocked(form.isAccountNonLocked())
                .accountExpiryAt(form.getAccountExpiryAt())
                .passwordExpiryAt(form.getPasswordExpiryAt())
                .build();
        
        // 利用者更新
        jdbcUsersDao.update(user);
        
        // 利用者権限の削除登録
        jdbcAuthoritiesDao.delete(user.getLoginId());
        form.getAuthorityIds().forEach(authorityId -> {
            var authority = new Authorities(form.getLoginId(), authorityId);
            jdbcAuthoritiesDao.insert(authority);
        });
    }
}
