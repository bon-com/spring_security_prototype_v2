package com.example.prototype.biz.users.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.prototype.biz.users.dao.JdbcAuthorityMasterDao;
import com.example.prototype.web.users.dto.AuthorityMasterDto;

@Service
public class AuthorityMasterService {
    @Autowired
    private JdbcAuthorityMasterDao jdbcAuthorityMasterDao;
    
    /**
     * 権限マスタ一覧取得
     * @return
     */
    public List<AuthorityMasterDto> findAllActive() {
        List<AuthorityMasterDto> authorityList = new ArrayList<>();
        
        jdbcAuthorityMasterDao.findAllActive().stream().forEach(a -> {
            var dto = new AuthorityMasterDto(a.getAuthorityId(), a.getAuthorityName());
            authorityList.add(dto);
        });
        
        return authorityList;
    }
    
}
