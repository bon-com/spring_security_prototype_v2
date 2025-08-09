package com.example.prototype.biz.debugger;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * Bean登録されたフィルターとBeanのデバッグ用
 */
@Component
public class FilterDebugger implements SmartInitializingSingleton {
    private static final Logger logger = LoggerFactory.getLogger(FilterDebugger.class);
    @Autowired
    @Lazy
    private FilterChainProxy filterChainProxy;
    @Autowired
    private ApplicationContext applicationContext;
    
    public void afterSingletonsInstantiated() {
        // 登録フィルター参照
        for (SecurityFilterChain chain : filterChainProxy.getFilterChains()) {
            if (chain instanceof DefaultSecurityFilterChain) {
                DefaultSecurityFilterChain defaultChain = (DefaultSecurityFilterChain) chain;
                logger.debug("\n◆◆ Filter chain for: [{}] ◆◆", defaultChain.getRequestMatcher());

                for (Filter filter : defaultChain.getFilters()) {
                    logger.debug("[DEBUG]  {}", filter.getClass().getSimpleName());
                }
            }
        }

        // 登録Bean参照
        logger.debug("\n◆◆ Registered Spring Beans ◆◆");
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            logger.debug("[DEBUG]  [{}] : {}", beanName, bean.getClass().getName());
        }
    }
}
