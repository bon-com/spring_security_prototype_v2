package com.example.prototype.biz.users.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.prototype.biz.users.entity.Authorities;

@Repository
public class JdbcAuthoritiesDao {
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(JdbcAuthoritiesDao.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    /** エンティティマッパー */
    private static final RowMapper<Authorities> authoritiesRowMapper = (rs, i) -> {
        var authorities = new Authorities();
        authorities.setLoginId(rs.getString("login_id"));
        authorities.setAuthorityId(rs.getInt("authority_id"));
        return authorities;
    };
    
    /**
     * 権限紐づきテーブル登録
     * @param authorities
     */
    public void insert(Authorities authorities) {
        var sql = new StringBuilder();
        sql.append("INSERT INTO authorities (");
        sql.append("login_id, authority_id");
        sql.append(") VALUES (");
        sql.append(":loginId, :authorityId)");
        var param = new BeanPropertySqlParameterSource(authorities);
        
        logger.debug(
                "\n★★SQL実行★★\n・クラス=JdbcAuthoritiesDao\n・メソッド=insert\n・SQL={}\n・パラメータ={}\n",
                sql, authorities);
        namedParameterJdbcTemplate.update(sql.toString(), param);
    }
    
    /**
     * ログインID検索
     * @param loginId
     * @return
     */
    public List<Authorities> findByLoginId(String loginId) {
        var sql = "SELECT login_id, authority_id FROM authorities WHERE login_id = :loginId";
        var param = new MapSqlParameterSource();
        param.addValue("loginId", loginId);

        logger.debug("\n★★SQL実行★★\n・クラス=JdbcAuthoritiesDao\n・メソッド=findByLoginId\n・SQL={}\n・パラメータ={}\n", sql, loginId);
        return namedParameterJdbcTemplate.query(sql.toString(), param, authoritiesRowMapper);
    }
    
    /**
     * 利用者権限削除
     * @param loginId
     */
    public void delete(String loginId) {
        var sql = new StringBuilder();
        sql.append("DELETE FROM authorities ");
        sql.append("WHERE login_id = :loginId");
        
        var param = new MapSqlParameterSource();
        param.addValue("loginId", loginId);
        
        logger.debug(
                "\n★★SQL実行★★\n・クラス=JdbcAuthoritiesDao\n・メソッド=delete\n・SQL={}\n・パラメータ={}\n",
                sql, loginId);
        namedParameterJdbcTemplate.update(sql.toString(), param);
    }
}
