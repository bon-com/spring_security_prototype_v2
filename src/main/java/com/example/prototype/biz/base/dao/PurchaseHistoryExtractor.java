package com.example.prototype.biz.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.example.prototype.base.entity.PurchaseHistory;
import com.example.prototype.base.entity.PurchaseItem;

/**
 * 購入履歴、購入商品履歴テーブルの結合情報をエンティティにマッピングするクラス
 */
public class PurchaseHistoryExtractor implements ResultSetExtractor<PurchaseHistory> {
    /**
     * マッピング処理
     */
    @Override
    public PurchaseHistory extractData(ResultSet rs) throws SQLException, DataAccessException {
        PurchaseHistory purchaseHistory = null;
        
        if (rs.next()) {
            // 購入履歴情報
            purchaseHistory = new PurchaseHistory();
            purchaseHistory.setId(rs.getInt("history_id"));
            purchaseHistory.setPurchaseDate(rs.getTimestamp("purchase_date").toLocalDateTime().toLocalDate());
            purchaseHistory.setItemList(new ArrayList<PurchaseItem>());

            do {
                // 購入商品履歴情報
                var purchaseItem = new PurchaseItem();
                purchaseItem.setId(rs.getInt("purchase_item_id"));
                purchaseItem.setPurchaseId(rs.getInt("purchase_id"));
                purchaseItem.setItemId(rs.getInt("item_id"));
                purchaseItem.setQuantity(rs.getInt("quantity"));
                purchaseItem.setPrice(rs.getInt("price") * rs.getInt("quantity"));
                purchaseItem.setItemName(rs.getString("item_name"));

                // 購入履歴情報に購入商品履歴情報を紐づけ
                addOrMergeItem(purchaseHistory, purchaseItem);
            } while (rs.next());
        }

        return purchaseHistory;
    }

    /**
     * マージ追加
     * 既に格納済みの購入商品履歴情報をマージする
     * @param history
     * @param newItem
     */
    private void addOrMergeItem(PurchaseHistory history, PurchaseItem newItem) {
        // 商品ID検索
        Optional<PurchaseItem> match = history.getItemList().stream()
                .filter(item -> item.getItemId() == newItem.getItemId())
                .findFirst();

        if (match.isPresent()) {
            // 既に格納済みの場合、数量と購入価格を更新
            PurchaseItem existing = match.get();
            int newQuantity = existing.getQuantity() + newItem.getQuantity();
            
            existing.setQuantity(newQuantity);
            existing.setPrice(newItem.getPrice() * newQuantity);
        } else {
            // 新規格納
            history.getItemList().add(newItem);
        }
    }

}
