package com.example.prototype.biz.base.dao;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.prototype.base.entity.PurchaseHistory;

@Repository
public class JdbcPurchaseHistoryDao {
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(JdbcPurchaseHistoryDao.class);
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 購入履歴１件登録
     * @param purchaseHistory
     * @return
     */
    public int insert(PurchaseHistory purchaseHistory) {
        var sql = "INSERT INTO purchase_history(purchase_date) VALUES (:purchaseDate)";
        var param = new BeanPropertySqlParameterSource(purchaseHistory);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        logger.debug("\n★★SQL実行★★\n・クラス=JdbcPurchaseHistoryDao\n・メソッド=insert\n・SQL={}\n・パラメータ={}\n", sql, param);
        namedParameterJdbcTemplate.update(sql, param, keyHolder);
        
        // 生成されたキー（id）を取得
        return keyHolder.getKey().intValue();
    }

    /**
     * 購入日付一覧検索
     * @return
     */
    public List<LocalDate> findAllPurchaseDate() {
        var sql = "SELECT DISTINCT purchase_date FROM purchase_history";
        
        logger.debug("\n★★SQL実行★★\n・クラス=JdbcPurchaseHistoryDao\n・メソッド=findAllPurchaseDate\n・SQL={}\n", sql);
        return namedParameterJdbcTemplate.query(sql, (rs, i) -> rs.getDate("purchase_date").toLocalDate());
    }

    /**
     * 購入日付をキーに購入履歴検索
     * @param purchaseDate
     * @return
     */
    public PurchaseHistory findByPurchaseDate(LocalDate purchaseDate) {
        var sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("h.id AS history_id, ");
        sql.append("h.purchase_date, ");
        sql.append("pi.id AS purchase_item_id, ");
        sql.append("pi.purchase_id, ");
        sql.append("pi.item_id, ");
        sql.append("pi.quantity, ");
        sql.append("pi.price, ");
        sql.append("i.name AS item_name ");
        sql.append("FROM purchase_history h ");
        sql.append("JOIN purchase_item pi ON h.id = pi.purchase_id ");
        sql.append("JOIN item i ON pi.item_id = i.id ");
        sql.append("WHERE h.purchase_date = :purchaseDate ");
        sql.append("ORDER BY h.id, pi.id");

        var param = new MapSqlParameterSource();
        param.addValue("purchaseDate", purchaseDate);

        logger.debug("\n★★SQL実行★★\n・クラス=JdbcPurchaseHistoryDao\n・メソッド=findByPurchaseDate\n・SQL={}\n・パラメータ={}\n", sql, param);
        return namedParameterJdbcTemplate.query(sql.toString(), param, new PurchaseHistoryExtractor());
    }

}
