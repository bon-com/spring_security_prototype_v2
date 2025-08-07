package com.example.prototype.web.users.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.prototype.common.constants.Constants;
import com.example.prototype.web.common.validator.FieldsMatch;

import lombok.Data;

/**
 * 利用者フォーム
 */
@Data
@FieldsMatch(field = "password", confirmField = "confirmPassword", errorField = "confirmPassword", message = Constants.ERR_MSG_PASSWORDS_NOT_MATCH)
public class UsersForm {
    /** ログインID */
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{2,50}$", message = "ログインIDは半角英数字2〜50文字で入力してください")
    private String loginId;
    /** 利用者氏名 */
    @NotNull
    @Size(min =1, max = 50)
    private String username;
    /** パスワード */
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{8,16}$", message = "パスワードは半角英数字8〜16文字で入力してください")
    private String password;
    /** 確認用パスワード */
    @NotNull
    private String confirmPassword;
    /** アカウント有効可否（true:有効/false:無効） */
    private boolean enabled = true;
    /** アカウントロック状態（true:ロック無し/false:ロック有り） */
    private boolean accountNonLocked = true;
    /** アカウント有効期限日時 */
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime accountExpiryAt;
    /** パスワード有効期限日時 */
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime passwordExpiryAt;
    /** 権限 */
    @NotEmpty
    private List<Integer> authorityIds;
}
