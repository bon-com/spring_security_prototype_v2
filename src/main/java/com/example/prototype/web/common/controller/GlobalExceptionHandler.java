package com.example.prototype.web.common.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.example.prototype.biz.utils.SessionCleaner;
import com.example.prototype.common.constants.Constants;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @Autowired
    private SessionCleaner sessionCleaner;
    
    @ExceptionHandler({ DataAccessException.class })
    public ModelAndView handleDataAccessException(DataAccessException ex, HttpSession session) {
        // 共通処理
        return handleCommonError(ex, session, Constants.ERR_MSG_500);
    }
    
    @ExceptionHandler({ IllegalStateException.class, IllegalArgumentException.class })
    public ModelAndView handleIllegalStateException(IllegalStateException ex, HttpSession session) {
        // 共通処理
        return handleCommonError(ex, session, Constants.ERR_MSG_DEFAULT);
    }
    
    /** エラー時の共通処理 */
    public ModelAndView handleCommonError(Exception ex, HttpSession session, String msg) {
        // セッションクリア
        sessionCleaner.clearSession(session);

        // ログ出力
        logger.error("\n★★エラー発生★★\n・種類: {}\n・内容: {}\n・例外： \n",
                ex.getClass().getSimpleName(),
                ex.getMessage(), ex);

        // エラーページへ
        var mav = new ModelAndView("error");
        mav.addObject("message", msg);
        
        return mav;
    }
    
}
