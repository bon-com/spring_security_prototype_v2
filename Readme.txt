◆Spring Securityについてのメモ
JavaConfigベースでやりなおす

◆認証用利用者
user01:passowrd
admin01:adminpass

◆SecurityConfigについて
◇SecurityFilterChainのBean定義
HttpSecurityを使って、認可・認証・ログアウトなどの設定ができる。
-----------------------
.authorizeHttpRequests(authz -> authz
        .requestMatchers(
                new AntPathRequestMatcher("/login"),
                new AntPathRequestMatcher("/oauth2/**"))
        .permitAll()
        .anyRequest().authenticated())
-----------------------
以下の意味
・/loginと/oauth2/**は誰でもアクセス可能（ログイン不要）
・それ以外のURLはすべて認証が必要（ログインしていないとアクセス不可）

