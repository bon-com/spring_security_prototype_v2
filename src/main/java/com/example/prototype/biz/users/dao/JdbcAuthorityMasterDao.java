package com.example.prototype.biz.users.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.prototype.biz.users.entity.AuthorityMaster;

@Repository
public class JdbcAuthorityMasterDao {
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(JdbcUsersDao.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    /** エンティティマッパー */
    private static final RowMapper<AuthorityMaster> authorityMasterRowMapper = (rs, i) -> {
        var master = new AuthorityMaster();
        master.setAuthorityId(rs.getInt("authority_id"));
        master.setAuthorityCode(rs.getString("authority_code"));
        master.setAuthorityName(rs.getString("authority_name"));
        master.setDisplayOrder(rs.getInt("display_order"));
        master.setActive(rs.getBoolean("is_active"));
        return master;
    };
    
    /**
     * 権限一覧取得
     * @return
     */
    public List<AuthorityMaster> findAllActive() {
        var sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("authority_id, ");
        sql.append("authority_code, ");
        sql.append("authority_name, ");
        sql.append("display_order, ");
        sql.append("is_active ");
        sql.append("FROM authority_master ");
        sql.append("WHERE is_active = TRUE ");
        sql.append("ORDER BY display_order ASC;");

        logger.debug("\n★★SQL実行★★\n・クラス=JdbcAuthorityMasterDao\n・メソッド=findAllActive\n・SQL={}\n・パラメータ={}\n", sql);
        return namedParameterJdbcTemplate.query(sql.toString(), authorityMasterRowMapper);
    }
}
