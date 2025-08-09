package com.example.prototype.common.constants;

/**
 * 定数クラス
 */
public class Constants {
    /** 認可エラーメッセージ */
    public static final String ERR_MSG_403 = "アクセス権限がありません";
    /** システムエラーメッセージ */
    public static final String ERR_MSG_500 = "システムエラーが発生しました";
    /** その他エラーメッセージ */
    public static final String ERR_MSG_DEFAULT = "予期せぬエラーが発生しました";
    /** 認証エラーメッセージ */
    public static final String ERR_MSG_AUTHENTICATION_BAD_CREDENTIALS = "ログインIDまたはパスワードが間違っています";
    /** パスワード相関チェックエラーメッセージ */
    public static final String ERR_MSG_PASSWORDS_NOT_MATCH = "パスワードと確認用パスワードが一致しません";
    /** 相関チェック異常エラーメッセージ */
    public static final String ERR_MSG_PROPERTY_ACCESS_FAILED = "指定されたフィールドの取得に失敗しました";
    /** ログインID重複エラーメッセージ */
    public static final String ERR_MSG_LOGIN_ID_DUPLICATE = "ログインIDが既に登録されています";
    /** メールアドレス重複エラーメッセージ */
    public static final String ERR_MSG_EMAIL_DUPLICATE = "メールアドレスが既に登録されています";
    /** セッションタイムアウトエラーメッセージ */
    public static final String ERR_MSG_INVALID_SESSION = "セッションが切れたため、再度ログインしてください";
    /** 利用者取得エラーメッセージ */
    public static final String ERR_MSG_NO_USER = "利用者の取得に失敗しました";
    /** 認証エラーエラーメッセージ（Google OAuth2ログイン） */
    public static final String ERR_MSG_GOOGLE_OAUTH_FAILURE = "Gmailが紐づいていません";
    /** 更新完了メッセージ */
    public static final String MSG_UPDATE_SUCCESS = "更新完了しました";
    /** 更新失敗メッセージ */
    public static final String ERR_MSG_UPDATE_FAILURE = "更新に失敗しました";
    /** Google紐づけ成功メッセージ */
    public static final String MSG_UPDATE_GOOGLE_SUB_SUCCESS = "Googleアカウントの紐づけ完了しました";
    /** Google紐づけ失敗メッセージ */
    public static final String ERR_MSG_UPDATE_GOOGLE_FAILURE = "Googleアカウントと登録メールアドレスが一致しません";
    /** Google紐づけ解除メッセージ */
    public static final String MSG_CANSEL_GOOGLE_SUB = "Googleアカウントの紐づけ状態を解除しました";
    /** 登録完了メッセージ */
    public static final String MSG_INSERT_SUCCESS = "登録完了しました";
    /** パスワード有効期限切れ事前通知メッセージ */
    public static final String MSG_PASSWORD_EXPIRY_TEMPLATE = "パスワードの有効期限まで%d日のため、パスワードを変更してください";
    /** 一度の商品購入限度数メッセージ */
    public static final String MSG_MAX_PURCHASE_QUANTITY = "一度の購入は10個までです";
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
