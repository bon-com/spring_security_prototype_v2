package com.example.prototype.web.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.prototype.biz.utils.MessageUtil;
import com.example.prototype.common.constants.Constants;

@Component
@ControllerAdvice("com.example.prototype.web")
public class CommonControllerAdvice {
    @Autowired
    private MessageUtil messageUtil;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        /*
         * StringTrimmerEditorは文字列の前後にある不要な空白（スペース、タブ、改行など）を自動的にトリミングする
         * また、コンストラクタにtrueを設定することで、トリミング後に空文字だった場合NULLに変換する
         * ※BeanバリデーションのNULLチェックのためにNULL変換する
         */
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @ModelAttribute("greeting")
    public String greeting() {
        // プロパティからメッセージ取得
        return messageUtil.getMessage(Constants.WELCOME_MSG_KEY);
    }
}
