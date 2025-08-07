package com.example.prototype.biz.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DataUtil {
    /**
     * LocalDateTimeをDateに変換する
     * @param target
     * @return
     */
    public static Date convertDateFromLocalDateTime(LocalDateTime target) {
        if (target == null) {
            return null;
        }
        
        return Date.from(target.atZone(ZoneId.systemDefault()).toInstant());
    }
}
