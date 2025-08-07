package com.example.prototype.web.users.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.prototype.web.users.dto.AuthorityMasterDto;

public class UsersViewHelper {
    /**
     * 利用者入力フォーム用の利用者権限一覧情報を取得
     * @param authorities
     * @return
     */
    public static List<Integer> convertAuthorities(List<AuthorityMasterDto> authorities) {
        List<Integer> resList = new ArrayList<>();
        authorities.forEach(a -> {
            resList.add(a.getAuthorityId());
        });
        
        return resList;
    }
}
