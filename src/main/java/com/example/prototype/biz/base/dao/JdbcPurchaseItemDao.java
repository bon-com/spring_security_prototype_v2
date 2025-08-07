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

import com.example.prototype.base.entity.PurchaseItem;

@Repository
public class JdbcPurchaseItemDao {
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(JdbcPurchaseItemDao.class);
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    /** エンティティマッパー */
    private static final RowMapper<PurchaseItem> purHistoryRowMapper = (rs, i) -> {
        var purchaseItem = new PurchaseItem();
        purchaseItem.setId(rs.getInt("id"));
        purchaseItem.setPurchaseId(rs.getInt("purchase_id"));
        purchaseItem.setItemId(rs.getInt("item_id"));
        purchaseItem.setQuantity(rs.getInt("quantity"));
        purchaseItem.setPrice(rs.getInt("price"));
        purchaseItem.setItemName(rs.getString("item_name"));
        return purchaseItem;
    };

    /**
     * 購入商品履歴１件登録
     * @param purchaseItem
     */
    public void insert(PurchaseItem purchaseItem) {
        var sql = "INSERT INTO purchase_item (purchase_id, item_id, quantity, price) VALUES (:purchaseId, :itemId, :quantity, :price);";
        var param = new BeanPropertySqlParameterSource(purchaseItem);

        logger.debug("\n★★SQL実行★★\n・クラス=JdbcPurchaseItemDao\n・メソッド=insert\n・SQL={}\n・パラメータ={}\n", sql, param);
        namedParameterJdbcTemplate.update(sql, param);
    }

    /**
     * IDをキーに購入商品履歴を検索
     * @param purchaseId
     * @return
     */
    public List<PurchaseItem> findByPurchaseId(int purchaseId) {
        var sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("p.id, ");
        sql.append("p.purchase_id, ");
        sql.append("p.item_id, ");
        sql.append("p.quantity, ");
        sql.append("p.price, ");
        sql.append("i.name AS item_name ");
        sql.append("FROM purchase_item p ");
        sql.append("JOIN item i ON p.item_id = i.id ");
        sql.append("WHERE p.purchase_id = :purchaseId");

        var param = new MapSqlParameterSource();
        param.addValue("purchaseId", purchaseId);

        logger.debug("\n★★SQL実行★★\n・クラス=JdbcPurchaseItemDao\n・メソッド=findByPurchaseId\n・SQL={}\n・パラメータ={}\n", sql, param);
        return namedParameterJdbcTemplate.query(sql.toString(), param, purHistoryRowMapper);
    }
}
