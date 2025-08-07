package com.example.prototype.biz.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {
    @Autowired
    private MessageSource messageSource;
    
    /**
     * メッセージの取得
     * @param key
     * @return
     */
    public String getMessage(String key) {
        return getMessage(key, null);
    }

    /**
     * メッセージの取得
     * @param key
     * @param args
     * @return
     */
    public String getMessage(String key, Object[] args) {
        return messageSource.getMessage(key, args, Locale.JAPANESE);
    }
}
