package com.example.prototype.common.constants;

/**
 * 定数クラス
 */
public class Constants {
    /** 認可エラーメッセージ */
    public static final String ERR_MSG_403 = "error.message.403";
    /** システムエラーメッセージ */
    public static final String ERR_MSG_500 = "error.message.500";
    /** その他エラーメッセージ */
    public static final String ERR_MSG_DEFAULT = "error.message.default";
    /** ログインID重複エラーメッセージ */
    public static final String ERR_MSG_LOGIN_ID_DUPLICATE = "error.message.login.id.duplicate";
    /** メールアドレス重複エラーメッセージ */
    public static final String ERR_MSG_EMAIL_DUPLICATE = "error.message.email.duplicate";
    /** セッションタイムアウトエラーメッセージ */
    public static final String ERR_MSG_INVALID_SESSION = "error.message.invalid.session";
    /** 利用者取得エラーメッセージ */
    public static final String ERR_MSG_NO_USER = "error.message.no.user";
    /** 認証エラーエラーメッセージ（Google OAuth2ログイン） */
    public static final String ERR_MSG_GOOGLE_OAUTH_FAILURE = "error.message.google.oauth.failure";
    /** 更新完了メッセージ */
    public static final String MSG_UPDATE_SUCCESS = "message.update.success";
    /** 更新失敗メッセージ */
    public static final String ERR_MSG_UPDATE_FAILURE = "error.message.update.failure";
    /** Google紐づけ成功メッセージ */
    public static final String MSG_UPDATE_GOOGLE_SUB_SUCCESS = "message.update.google.sub.success";
    /** Google紐づけ失敗メッセージ */
    public static final String ERR_MSG_UPDATE_GOOGLE_FAILURE = "error.message.update.google.failure";
    /** Google紐づけ解除メッセージ */
    public static final String MSG_CANSEL_GOOGLE_SUB = "message.cansel.google.sub";
    /** 登録完了メッセージ */
    public static final String MSG_INSERT_SUCCESS = "message.insert.success";
    /** パスワード有効期限切れ事前通知メッセージ */
    public static final String MSG_PASSWORD_EXPIRY_TEMPLATE = "message.password.expiry.template";
    /** 一度の商品購入限度数メッセージ */
    public static final String MSG_MAX_PURCHASE_QUANTITY = "message.max.purchase.quantity";
    /** アカウントロック種別：ロックあり */
    public static final String ACCOUNT_LOCKED = "ロックあり";
    /** アカウントロック種別：ロックなし */
    public static final String ACCOUNT_NOT_LOCKED = "ロックなし";
    /** 認証者用ウェルカムメッセージキー */
    public static final String WELCOME_MSG_KEY = "welcome.msg";
    /** 更新成功メッセージキー */
    public static final String UPDATE_SUCCESS_KEY = "update-success";
    /** 登録成功メッセージキー */
    public static final String INSERT_SUCCESS_KEY = "insert-success";
    /** Google紐づけ成功メッセージキー */
    public static final String GOOGLE_LINK_SUCCESS_KEY = "google-link-success";
    /** Google紐づけ失敗メッセージキー */
    public static final String GOOGLE_LINK_FAILURE_KEY = "google-link-failure";
    /** Google紐づけ解除メッセージキー */
    public static final String GOOGLE_LINK_CANSEL_KEY = "google-link-cansel";
    /** 一度の商品購入限度数 */
    public static final int MAX_PURCHASE_QUANTITY = 10;
    /** マスキング項目 */
    public static final String MASKING_ITEM = "*****";
    /** Google連携（ログイン） */
    public static final String GOOGLE_REGISTRATION_ID_LOGIN = "google-login";
    /** Google連携（紐づけ） */
    public static final String GOOGLE_REGISTRATION_ID_LINK = "google-link";
}
