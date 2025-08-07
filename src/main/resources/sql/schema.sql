-- 商品テーブル
CREATE TABLE item (
    id INT AUTO_INCREMENT PRIMARY KEY,        -- 商品ID
    name VARCHAR(100) NOT NULL,               -- 商品名
    price INT NOT NULL,                       -- 値段
    deleted BOOLEAN NOT NULL DEFAULT FALSE    -- 論理削除フラグ（false: 有効, true: 削除済）
);

-- 購入履歴テーブル
CREATE TABLE purchase_history (
    id INT AUTO_INCREMENT PRIMARY KEY,        -- 購入履歴ID
    purchase_date DATE NOT NULL               -- 購入日付
);

-- 購入商品履歴テーブル
CREATE TABLE purchase_item (
    id INT AUTO_INCREMENT PRIMARY KEY,         -- 購入商品ID
    purchase_id INT NOT NULL,                  -- 購入履歴ID（外部キー）
    item_id INT NOT NULL,                      -- 商品ID（外部キー）
    quantity INT NOT NULL,                     -- 購入数量
    price INT NOT NULL,                        -- 購入時の値段
    FOREIGN KEY (purchase_id) REFERENCES purchase_history(id),
    FOREIGN KEY (item_id) REFERENCES item(id)
);

-- 利用者テーブル
CREATE TABLE users (
  login_id VARCHAR(50) PRIMARY KEY,                     -- ログインID 
  username VARCHAR(50) NOT NULL,                        -- 利用者指名
  password VARCHAR(100) NOT NULL,                        -- パスワード
  enabled BOOLEAN NOT NULL,                             -- アカウント有効可否（true:有効/false:無効）
  account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,     -- アカウントロック状態（true:ロック無し/false:ロック有り）
  login_failure_count INT NOT NULL DEFAULT 0,           -- ログイン失敗回数
  last_login_at TIMESTAMP,                              -- 最終ログイン日時
  account_expiry_at TIMESTAMP NOT NULL,                 -- アカウント有効期限日時
  password_expiry_at TIMESTAMP NOT NULL                 -- パスワード有効期限日時
);

-- 権限マスタ
CREATE TABLE authority_master (
    authority_id INT PRIMARY KEY AUTO_INCREMENT,        -- 権限マスタID
    authority_code VARCHAR(50) NOT NULL UNIQUE,         -- 権限コード（例: ROLE_ADMIN）
    authority_name VARCHAR(100) NOT NULL,               -- 権限表示名
    display_order INT,                                  -- 表示順
    is_active BOOLEAN DEFAULT TRUE                      -- 権限マスタ有効可否（true:有効/false:無効）
);

-- 権限紐づきテーブル
CREATE TABLE authorities (
  login_id VARCHAR(50) NOT NULL,                        -- ログインID
  authority_id INT NOT NULL,                            -- 権限マスタID
  PRIMARY KEY (login_id, authority_id),
  FOREIGN KEY (login_id) REFERENCES users(login_id),
  FOREIGN KEY (authority_id) REFERENCES authority_master(authority_id)
);

