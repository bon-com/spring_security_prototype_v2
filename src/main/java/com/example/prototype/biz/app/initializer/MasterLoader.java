package com.example.prototype.biz.app.initializer;

import java.util.List;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.prototype.biz.users.service.AuthorityMasterService;
import com.example.prototype.web.users.dto.AuthorityMasterDto;

@Component
public class MasterLoader implements SmartInitializingSingleton {
    @Autowired
    private AuthorityMasterService authorityMasterService;

    /** 権限マスタ一覧 */
    private List<AuthorityMasterDto> cachedAuthorityList;
    
    /**
     * すべてのシングルトンBeanの初期化完了後に実行
     */
    @Override
    public void afterSingletonsInstantiated() {
        // 初期化
        this.cachedAuthorityList = authorityMasterService.findAllActive();
    }

    /**
     * 権限マスタ一覧取得
     * @return
     */
    public List<AuthorityMasterDto> getCachedAuthorityList() {
        return cachedAuthorityList;
    }
}
