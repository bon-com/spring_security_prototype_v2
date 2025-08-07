package com.example.prototype.biz.users.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.example.prototype.biz.users.entity.ExtendedUser;
import com.example.prototype.common.constants.Constants;

@Repository
public class JdbcUsersDao {
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(JdbcUsersDao.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /** エンティティマッパー（1件取得用） */
    private final ResultSetExtractor<ExtendedUser> userExtractor = rs -> {
        if (!rs.next()) {
            throw new UsernameNotFoundException(Constants.ERR_MSG_AUTHENTICATION_BAD_CREDENTIALS);
        }

        var builder = ExtendedUser.builder()
        .loginId(rs.getString("login_id")) // ログインID
        .username(rs.getString("username")) // 利用者氏名
        .password(rs.getString("password")) // パスワード
        .enabled(rs.getBoolean("enabled")) // enabled（アカウント有効可否）
        .accountNonLocked(rs.getBoolean("account_non_locked")) // アカウントロック状態（true:ロック無し/false:ロック有り）
        .loginFailureCount(rs.getInt("login_failure_count")) // ログイン失敗回数
        .lastLoginAt(rs.getTimestamp("last_login_at") != null // 最終ログイン日時
                ? rs.getTimestamp("last_login_at").toLocalDateTime()
                : null)
        .accountExpiryAt(rs.getTimestamp("account_expiry_at").toLocalDateTime()) // アカウント有効期限日時
        .passwordExpiryAt(rs.getTimestamp("password_expiry_at").toLocalDateTime()); // パスワード有効期限日時
        
        do {
            String authority = rs.getString("authority");
            if (authority != null) {
                builder.addAuthority(new SimpleGrantedAuthority(authority));
            }
        } while (rs.next());

        return builder.build();
    };

    /** エンティティマッパー（リスト用） */
    private ResultSetExtractor<List<ExtendedUser>> userListExtractor = rs -> {
        // 拡張ユーザーマップ（key:ログインID、val:ビルダー）
        Map<String, ExtendedUser.Builder> userBuilderMap = new LinkedHashMap<>();

        while (rs.next()) {
            // ビルダー取得
            String loginId = rs.getString("login_id");
            var builder = userBuilderMap.get(loginId);

            if (builder == null) {
                // ビルダー生成
                builder = ExtendedUser.builder()
                        .loginId(loginId)
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .enabled(rs.getBoolean("enabled"))
                        .accountNonLocked(rs.getBoolean("account_non_locked"))
                        .loginFailureCount(rs.getInt("login_failure_count"))
                        .lastLoginAt(rs.getTimestamp("last_login_at") != null
                                ? rs.getTimestamp("last_login_at").toLocalDateTime()
                                : null)
                        .accountExpiryAt(rs.getTimestamp("account_expiry_at").toLocalDateTime())
                        .passwordExpiryAt(rs.getTimestamp("password_expiry_at").toLocalDateTime());

                userBuilderMap.put(loginId, builder);
            }

            // 権限追加
            String authority = rs.getString("authority");
            if (authority != null) {
                builder.addAuthority(new SimpleGrantedAuthority(authority));
            }
        }

        // BuilderからExtendedUserに変換
        return userBuilderMap.values().stream()
                .map(ExtendedUser.Builder::build)
                .collect(Collectors.toList());

    };

    /**
     * ログインID検索
     * @param username
     * @return
     */
    public ExtendedUser findByLoginId(String loginId) {
        var sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("u.login_id AS login_id, ");
        sql.append("u.username AS username, ");
        sql.append("u.password AS password, ");
        sql.append("u.enabled AS enabled, ");
        sql.append("u.account_non_locked AS account_non_locked, ");
        sql.append("u.login_failure_count AS login_failure_count, ");
        sql.append("u.last_login_at AS last_login_at, ");
        sql.append("u.account_expiry_at AS account_expiry_at, ");
        sql.append("u.password_expiry_at AS password_expiry_at, ");
        sql.append("am.authority_code AS authority ");
        sql.append("FROM users u ");
        sql.append("INNER JOIN authorities a ON u.login_id = a.login_id ");
        sql.append("INNER JOIN authority_master am ON a.authority_id = am.authority_id ");
        sql.append("WHERE u.login_id = :loginId");

        var param = new MapSqlParameterSource();
        param.addValue("loginId", loginId);

        logger.debug("\n★★SQL実行★★\n・クラス=JdbcUsersDao\n・メソッド=findByLoginId\n・SQL={}\n・パラメータ={}\n", sql, loginId);
        return namedParameterJdbcTemplate.query(sql.toString(), param, userExtractor);
    }

    /**
     * 認証情報更新
     * アカウントロック状態、ログイン失敗回数、最終ログイン日時を更新する
     * @param user
     */
    public void updateAuthStatus(ExtendedUser user) {
        // 更新対象の存在チェック
        int count = findCountByLoginId(user.getLoginId());
        if (count == 0) {
            // 更新対象なし
            throw new IllegalStateException(Constants.MSG_UPDATE_ERR + ": loginId=" + user.getLoginId());
        }

        // 認証情報更新
        var sql = new StringBuilder();
        sql.append("UPDATE users SET ");
        sql.append("enabled = :enabled, ");
        sql.append("account_non_locked = :accountNonLocked, ");
        sql.append("login_failure_count = :loginFailureCount, ");
        sql.append("last_login_at = :lastLoginAt ");
        sql.append("WHERE login_id = :loginId");
        var param = new BeanPropertySqlParameterSource(user);

        logger.debug(
                "\n★★SQL実行★★\n・クラス=JdbcUsersDao\n・メソッド=updateAuthStatus\n・SQL={}\n・パラメータ={}\n",
                sql, user);
        namedParameterJdbcTemplate.update(sql.toString(), param);
    }

    /**
     * パスワード更新
     * @param user
     */
    public void updatePassword(ExtendedUser user) {
        // 更新対象の存在チェック
        String loginId = user.getLoginId();
        int count = findCountByLoginId(loginId);
        if (count == 0) {
            // 更新対象なし
            throw new IllegalStateException(Constants.MSG_UPDATE_ERR + ": loginId=" + loginId);
        }

        var sql = new StringBuilder();
        sql.append("UPDATE users SET ");
        sql.append("password = :password, ");
        sql.append("password_expiry_at = :passwordExpiryAt ");
        sql.append("WHERE login_id = :loginId");
        var param = new BeanPropertySqlParameterSource(user);

        logger.debug(
                "\n★★SQL実行★★\n・クラス=JdbcUsersDao\n・メソッド=updatePassword\n・SQL={}\n・パラメータ={{ password_expiry_at={} loginId={} }}\n",
                sql, user.getPasswordExpiryAt(), loginId);
        namedParameterJdbcTemplate.update(sql.toString(), param);
    }

    /**
     * ログインID検索
     * 検索結果件数を返却
     * @param loginId
     * @return
     */
    public int findCountByLoginId(String loginId) {
        var sql = "SELECT COUNT(*) FROM users WHERE login_id = :loginId";
        var param = new MapSqlParameterSource("loginId", loginId);

        logger.debug("\n★★SQL実行★★\n・クラス=JdbcUsersDao\n・メソッド=findCountByLoginId\n・SQL={}\n・パラメータ={}\n",
                sql, loginId);
        return namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class);
    }

    /**
     * 利用者一覧取得
     * @return
     */
    public List<ExtendedUser> findAll() {
        var sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("u.login_id as login_id, ");
        sql.append("u.username as username, ");
        sql.append("u.password as password, ");
        sql.append("u.enabled as enabled, ");
        sql.append("u.account_non_locked as account_non_locked, ");
        sql.append("u.login_failure_count as login_failure_count, ");
        sql.append("u.last_login_at as last_login_at, ");
        sql.append("u.account_expiry_at as account_expiry_at, ");
        sql.append("u.password_expiry_at as password_expiry_at, ");
        sql.append("am.authority_code as authority ");
        sql.append("FROM users u ");
        sql.append("INNER JOIN authorities a ON u.login_id = a.login_id ");
        sql.append("INNER JOIN authority_master am ON a.authority_id = am.authority_id"); // ← 追加

        logger.debug("\n★★SQL実行★★\n・クラス=JdbcUsersDao\n・メソッド=findAll\n・SQL={}\n", sql);
        return namedParameterJdbcTemplate.query(sql.toString(), userListExtractor);
    }
    
    /**
     * 利用者テーブル登録
     * @param user
     */
    public void insert(ExtendedUser user) {
        var sql = new StringBuilder();
        sql.append("INSERT INTO users (");
        sql.append("login_id, username, password, enabled, account_non_locked, ");
        sql.append("login_failure_count, account_expiry_at, password_expiry_at");
        sql.append(") VALUES (");
        sql.append(":loginId, :username, :password, :enabled, :accountNonLocked, ");
        sql.append(":loginFailureCount, :accountExpiryAt, :passwordExpiryAt");
        sql.append(")");
        var param = new BeanPropertySqlParameterSource(user);

        logger.debug(
                "\n★★SQL実行★★\n・クラス=JdbcUsersDao\n・メソッド=insert\n・SQL={}\n・パラメータ={}\n",
                sql, user);
        namedParameterJdbcTemplate.update(sql.toString(), param);
    }
    
    /**
     * 利用者情報更新
     * @param user
     */
    public void update(ExtendedUser user) {
        // 利用者チェック
        String loginId = user.getLoginId();
        int count = findCountByLoginId(user.getLoginId());
        if (count == 0) {
            // 更新対象なし
            throw new IllegalStateException(Constants.MSG_UPDATE_ERR + ": loginId=" + loginId);
        }
        
        var sql = new StringBuilder();
        sql.append("UPDATE users SET ");
        sql.append("username = :username, ");
        sql.append("password = :password, ");
        sql.append("enabled = :enabled, ");
        sql.append("account_non_locked = :accountNonLocked, ");
        sql.append("login_failure_count = :loginFailureCount, ");
        sql.append("account_expiry_at = :accountExpiryAt, ");
        sql.append("password_expiry_at = :passwordExpiryAt ");
        sql.append("WHERE login_id = :loginId");
        var param = new BeanPropertySqlParameterSource(user);

        logger.debug(
                "\n★★SQL実行★★\n・クラス=JdbcUsersDao\n・メソッド=update\n・SQL={}\n・パラメータ={}\n",
                sql, user);
        namedParameterJdbcTemplate.update(sql.toString(), param);
    }
}
