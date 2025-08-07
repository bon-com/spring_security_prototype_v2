package com.example.prototype.biz.base.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.prototype.base.entity.Item;
import com.example.prototype.common.constants.Constants;

@Repository
public class JdbcItemDao {
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(JdbcItemDao.class);
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /** エンティティマッパー */
    private static final RowMapper<Item> itemRowMapper = (rs, i) -> {
        var item = new Item();
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setPrice(rs.getInt("price"));
        item.setDeleted(rs.getBoolean("deleted"));
        return item;
    };

    /**
     * 商品全件検索
     * @return
     */
    public List<Item> findAll() {
        var sql = "SELECT id, name, price, deleted FROM item WHERE deleted = false";
        
        logger.debug("\n★★SQL実行★★\n・クラス=JdbcItemDao\n・メソッド=findAll\n・SQL={}\n", sql);
        return namedParameterJdbcTemplate.query(sql, itemRowMapper);
    }

    /**
     * 商品ID検索
     * @param id
     * @return
     */
    public Item findById(int id) {
        var sql = "SELECT id, name, price, deleted FROM item WHERE id = :id and deleted = false";
        var param = new MapSqlParameterSource();
        param.addValue("id", id);

        logger.debug("\n★★SQL実行★★\n・クラス=JdbcItemDao\n・メソッド=findById\n・SQL={}\n・パラメータ={}\n", sql, param);
        return namedParameterJdbcTemplate.queryForObject(sql, param, itemRowMapper);
    }
    
    /**
     * 商品削除フラグ更新（管理者用）
     * @param id
     */
    public void updateDeletedByAdmin(int id, boolean deleted) {
        var sql = "UPDATE item SET deleted = :deleted WHERE id = :id";
        var param = new MapSqlParameterSource();
        param.addValue("id", id);
        param.addValue("deleted", deleted);
        
        logger.debug("\n★★SQL実行★★\n・クラス=JdbcItemDao\n・メソッド=updateDeleted\n・SQL={}\n・パラメータ={}\n", sql, id);
        int count = namedParameterJdbcTemplate.update(sql, param);
        if (count == 0) {
            throw new IllegalStateException(Constants.MSG_UPDATE_ERR);
        }
    }

    /**
     * 商品全件検索（管理者用）
     * @return
     */
    public List<Item> findAllByAdmin() {
        var sql = "SELECT id, name, price, deleted FROM item";
        
        logger.debug("\n★★SQL実行★★\n・クラス=JdbcItemDao\n・メソッド=findAllByAdmin\n・SQL={}\n", sql);
        return namedParameterJdbcTemplate.query(sql, itemRowMapper);
    }
    
    /**
     * 商品登録
     * @param item
     */
    public void insert(Item item) {
        var sql = "INSERT INTO item (name, price, deleted) VALUES (:name, :price, :deleted)";
        var param = new BeanPropertySqlParameterSource(item);
        
        logger.debug("\n★★SQL実行★★\n・クラス=JdbcItemDao\n・メソッド=insert\n・SQL={}\n・パラメータ={}\n", sql, param);
        namedParameterJdbcTemplate.update(sql, param);
    }
}
