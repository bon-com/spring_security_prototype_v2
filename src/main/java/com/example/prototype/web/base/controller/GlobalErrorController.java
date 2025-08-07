package com.example.prototype.web.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.prototype.common.constants.Constants;

@Controller
@RequestMapping("system")
public class GlobalErrorController {
    /**
     * 共通エラー画面表示
     * @param code
     * @param model
     * @return
     */
    @GetMapping(value = "/error")
    public String handleError(
        @RequestParam(name = "code", required = false) String code, Model model) {
        String message = "";
        switch (code) {
            case "403":
                message = Constants.ERR_MSG_403;
                break;
            case "500":
                message = Constants.ERR_MSG_500;
                break;
            default:
                message = Constants.ERR_MSG_DEFAULT;
                break;
        }
        model.addAttribute("message", message);
        
        return "error";
    }

}
