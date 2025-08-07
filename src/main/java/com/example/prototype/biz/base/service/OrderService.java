package com.example.prototype.biz.base.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.prototype.base.entity.PurchaseHistory;
import com.example.prototype.base.entity.PurchaseItem;
import com.example.prototype.biz.base.dao.JdbcPurchaseHistoryDao;
import com.example.prototype.biz.base.dao.JdbcPurchaseItemDao;
import com.example.prototype.web.base.dto.CartDto;

/**
 * 注文サービス
 */
@Service
public class OrderService {
    @Autowired
    private JdbcPurchaseHistoryDao jdbcPurchaseHistoryDao;

    @Autowired
    private JdbcPurchaseItemDao jdbcPurchaseItemDao;

    /**
     * 購入履歴に追加
     * @param cart
     * @param totalPrice
     */
    public void insertPurchaseHistory(CartDto cart, int totalPrice) {
        // 購入履歴生成
        var history = new PurchaseHistory();
        history.setPurchaseDate(LocalDate.now());
        List<PurchaseItem> purchaceItemList = createPurchaseItemList(cart);
        history.setItemList(purchaceItemList);

        // 購入履歴 DB登録
        int id = jdbcPurchaseHistoryDao.insert(history);

        // 購入商品履歴 DB登録
        purchaceItemList.forEach(pItem -> {
            pItem.setPurchaseId(id);
            jdbcPurchaseItemDao.insert(pItem);
        });
    }

    /**
     * カート情報から購入商品履歴情報を生成
     * @param cart
     * @return
     */
    private List<PurchaseItem> createPurchaseItemList(CartDto cart) {
        // 購入商品リスト生成
        List<PurchaseItem> purchaceItemList = new ArrayList<>();
        cart.getItems().values().forEach(cartItem -> {
            var entity = new PurchaseItem();
            var itemDto = cartItem.getItem();
            entity.setItemId(itemDto.getId());
            entity.setPrice(itemDto.getPrice());
            entity.setTotalPrice(cartItem.getTotal());
            entity.setQuantity(cartItem.getQuantity());

            purchaceItemList.add(entity);
        });

        return purchaceItemList;
    }
}
