package com.example.prototype.biz.users.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.prototype.biz.users.dao.JdbcAuthoritiesDao;
import com.example.prototype.web.users.dto.AuthoritiesDto;

@Service
public class AuthoritiesService {
    @Autowired
    private JdbcAuthoritiesDao jdbcAuthoritiesDao;
    
    /**
     * ログインID検索
     * @param loginId
     * @return
     */
    public List<AuthoritiesDto> findByLoginId(String loginId) {
        List<AuthoritiesDto> authoritiesList = new ArrayList<>();
        
        jdbcAuthoritiesDao.findByLoginId(loginId).forEach(authorities -> {
            var dto = new AuthoritiesDto();
            BeanUtils.copyProperties(authorities, dto);
            authoritiesList.add(dto);
        });
        
        return authoritiesList;
    }
}
