package com.example.prototype.biz.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.prototype.base.entity.Item;
import com.example.prototype.biz.base.dao.JdbcItemDao;
import com.example.prototype.web.base.dto.ItemDto;
import com.example.prototype.web.base.dto.ItemForm;

/**
 * 商品サービス
 */
@Service
public class ItemService {
    @Autowired
    private JdbcItemDao jdbcItemDao;

    /**
     * 商品一覧取得
     * @return
     */
    public List<ItemDto> findAll() {
        List<ItemDto> itemList = new ArrayList<>();

        jdbcItemDao.findAll().forEach(item -> {
            var dto = new ItemDto();
            BeanUtils.copyProperties(item, dto);
            itemList.add(dto);
        });

        return itemList;
    }

    /**
     * 商品ID検索
     * @param id
     * @return
     */
    public ItemDto findById(int id) {
        Item res = jdbcItemDao.findById(id);
        var dto = new ItemDto();
        BeanUtils.copyProperties(res, dto);

        return dto;
    }
    
    /**
     * 商品削除フラグ更新（管理者用）
     * @param id
     * @param deleted
     */
    public void updateDeletedByAdmin(int id, boolean deleted) {
        jdbcItemDao.updateDeletedByAdmin(id, deleted);
    }
    
    /**
     * 商品一覧取得（管理者用）
     * @return
     */
    public List<ItemDto> findAllByAdmin() {
        List<ItemDto> itemList = new ArrayList<>();

        jdbcItemDao.findAllByAdmin().forEach(item -> {
            var dto = new ItemDto();
            BeanUtils.copyProperties(item, dto);
            itemList.add(dto);
        });

        return itemList;
    }
    
    /**
     * 商品登録
     * @param itemDto
     */
    public void insert(ItemForm form) {
        var item = new Item();
        BeanUtils.copyProperties(form, item);
        
        jdbcItemDao.insert(item);
    }
}
