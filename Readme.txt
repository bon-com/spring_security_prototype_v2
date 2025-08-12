◆Spring Securityについてのメモ
JavaConfigベースでやりなおす

◆Spring Securityについてのメモ

◆認証用利用者
user01:passowrd
admin01:adminpass

◆認証（Authentication）とは？
ログイン画面でユーザー名とパスワードを入力し、利用者テーブル（users）などのDBと照合して本人確認を行うこと

◆applicationContext-security.xmlの補足
Spring Security機能を使用するため、Bean定義を行う。

補足１：<sec:http pattern="/resources/**" security="none" />
　⇒静的リソースをセキュリティ対象外とする
　⇒ログインしていなくても、resources配下にアクセス可能
　⇒この設定がないとログイン前にCSSが崩れたりすることがあるらしい

補足２：<sec:http>内部
　⇒「<intercept-url pattern="/**"」は、すべてのURLに対して認証を要求する
　⇒「access="isAuthenticated()"」は、ログイン済みであればアクセス可能

補足３：<form-login />のみ定義
　⇒Spring Security提供のデフォルトのログインフォームを使用する
　⇒「/login」がログインページ
　⇒認証成功後はデフォルトで「/」にリダイレクト
　※カスタムログインページを使いたい場合はlogin-page属性を指定する
　※リダイレクト先URLを定義したい場合、default-target-url属性を指定する

補足４：<sec:authentication-manager />
　⇒認証処理の中心となるAuthenticationManagerを定義
　⇒DB認証を使う場合は<authentication-provider>を追加する必要あり

補足５：DIコンテナに登録
作成したapplicationContext-security.xmlをweb.xmlのコンテキストローダーに指定する

◆サーブレットフィルタの補足
Spring Securityが提供しているサーブレットフィルタをweb.xmlに登録する

※applicationContext-security.xmlにてhttpタグを定義すると、
SpringSecurityの設定が構成され、結果としてspringSecurityFilterChainという名前のフィルタBeanが内部で作成される
このBeanをweb.xml（サーブレットコンテナ）に登録して有効にする

補足１：
<filter-name>springSecurityFilterChain</filter-name>
<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
　⇒DelegatingFilterProxyを使用して、DIコンテナで管理しているBeanをサーブレットコンテナに登録する
　⇒springSecurityFilterChainはSpring Securityが内部で管理しているBeanの名前を指定している
　⇒このBeanはapplicationContext-security.xmlに定義した内容を保持するBeanとなり、サーブレットコンテナで動作する

補足２：
<filter-name>springSecurityFilterChain</filter-name>
<url-pattern>/*</url-pattern>
Spring Securityを適用させるURLのパターン
上記はすべてのURLパターンに対してSpring Securityを有効にしている（ただし、resources配下を覗く）


◆処理の流れ（デフォルトのログイン画面を使用）
1.Spring Securityのフィルターがリクエストを検査
　　springSecurityFilterChainにより、全てのリクエストはSpring Securityのフィルターを通る
　　認証が必要なURLに対して未認証の場合、デフォルトで/loginにリダイレクト

2.ログインフォームから認証要求を送信（POST /login）

3.UsernamePasswordAuthenticationFilterが起動
　　POST /loginリクエストをこのフィルターが処理
　　AuthenticationManager（実体はProviderManager）に認証要求

4.AuthenticationManager → DaoAuthenticationProviderを呼び出す
　　AuthenticationManagerは複数の認証オブジェクトを保持する
　　順次この認証オブジェクトを呼び出して、認証処理を行う
　　デフォルトの認証オブジェクトでは、DaoAuthenticationProviderがある

5.DaoAuthenticationProviderがUserDetailsServiceを呼び出す
　　applicationContext-security.xmlの<sec:authentication-provider>タグに定義したカスタムUserDetailsServiceを呼ぶ
　　loadUserByUsername(Stringusername)メソッドを呼び出して、ユーザー情報（UserDetails）を取得

6.UserDetailsServiceでDBからユーザー情報を取得
　　該当ユーザーがいなければUsernameNotFoundExceptionがスローされ、認証失敗

7.PasswordEncoderを使ってパスワード検証
　　DBから取得したUserDetailsのpasswordと、フォームからのパスワードを比較
　　不一致ならBadCredentialsExceptionがスローされ、ログイン失敗

8.認証成功時
　　SecurityContextHolder.getContext().setAuthentication(...)でSecurityContextに認証情報を保存
　　HttpSessionSecurityContextRepositoryによりHttpSessionに保存される

9.ログイン成功後のリダイレクト
　　applicationContext-security.xmlのdefault-target-urlが設定されていれば、そこへリダイレクト
　　指定がなければ「/」にリダイレクト

◆その他
・CSRFトークン
Spring Securityは、POST・PUT・DELETEなどの状態変更リクエストに対してCSRFトークンの検証を行う。
そのトークンがリクエストに含まれていないまたは一致しない場合、403を返すらしい。
⇒解決策：Spring Securityが自動で用意するCSRFトークンをフォームに埋め込む
⇒<sec:csrfInput/>をフォーム内に埋め込む

・{noop}
data.sqlのパスワードの前に{noop}を付与している
これはハッシュ化せず平文として使用するという意味

・認証ユーザー名を画面表示
【JSTL ＋ Spring Securityタグライブラリ】を使用することで認証したユーザーにアクセスできる
「<sec:authentication property="name" />」とすることで、Authentication#getName()（※UserDetails.getUsername()）にアクセスできる

・パスワードのハッシュ化
applicationContext-security.xmlにパスコードエンコーダーのBeanを用意して
「sec:authentication-manager」に参照させることでパスワードをハッシュ化して管理できる
認証処理にて内部でBCryptPasswordEncoderのmatchesメソッドを呼び出し、入力値とDBにあるパスワードのハッシュ値を照合する

・ログアウト
<sec:logout logout-url="/logout" logout-success-url="/" invalidate-session="true" />
上記を設定するだけでログアウト機能が有効になる
ログアウト時は「/logout」にリクエストし、ログアウト成功時は「 / 」にリクエストする
また、invalidate-sessionをtrueにすることで、内部でsession.invalidate()が呼ばれてセッション破棄される

・認証情報をハンドラ引数で取得したいとき
１．<mvc:annotation-driven>にAuthenticationPrincipalArgumentResolverのBeanを追加
２．ハンドラ引数にて「@AuthenticationPrincipal」を付与して取得したい型を指定する

・認証時のUserDetailsの拡張
usersテーブルに「ロック状態」「失敗回数（ログイン）」「最終ログイン成功日時」を追加した
それに伴い、org.springframework.security.core.userdetails.UserをラップしたExtendedUserを用意した
ログイン失敗回数と最終ログイン日時を保持させている
ロック状態はデフォルトでフィールドにある
※追記：
Userの場合、アカウントロック状態などにアクセスできず利便性が悪いため、
org.springframework.security.core.userdetails.UserDetailsをラップした

・エラーメッセージ定義
認証エラー時のSpring Securityが用意しているデフォルトエラーメッセージは、プロパティに定義することでメッセージ変更可能
※以下参考
AbstractUserDetailsAuthenticationProvider.badCredentials=ログインIDまたはパスワードが間違っています
AbstractUserDetailsAuthenticationProvider.locked=アカウントはロックされています
AbstractUserDetailsAuthenticationProvider.disabled=アカウントは使用できません
AbstractUserDetailsAuthenticationProvider.expired=アカウントの有効期限が切れています
AbstractUserDetailsAuthenticationProvider.credentialsExpired=パスワードの有効期限が切れています

・自作ログイン画面追加
ログイン画面遷移用のコントローラー（AuthenticationController）、ログイン画面（login.jsp）、ログイン画面マッピング（applicationContext-security.xml）
を追加修正することで、自作ログイン画面に遷移させる

・自作ログイン画面のフォーム変数変更
ログインフォームから送信されるリクエストパラメータはデフォルトで「username」と「password	」となっている
この両者のパラメータを変更したい場合、<sec:form-loginに「username-parameter」属性と「password-parameter」属性を使用する

・認証イベントのハンドリング
１．認証成功イベント： InteractiveAuthenticationSuccessEvent
　　ルートアプリケーションコンテキスト配下にイベント取得用のBeanを用意する
　　@EventListenerを付与したメソッドの引数に上記イベントを指定することで、画面遷移を除いた認証処理がすべて成功した通知を取得できる

２．認証失敗イベント： AbstractAuthenticationFailureEvent
　　すべての認証失敗イベントの親クラス
　　AbstractAuthenticationFailureEvent
　　├── AuthenticationFailureBadCredentialsEvent
　　├── AuthenticationFailureDisabledEvent
　　├── AuthenticationFailureLockedEvent
　　├── AuthenticationFailureExpiredEvent
　　├── AuthenticationFailureCredentialsExpiredEvent
　　├── AuthenticationFailureProviderNotFoundEvent
　　├── AuthenticationFailureServiceExceptionEvent
　　※ただし、UsernameNotFoundExceptionがスローされた場合（usernameがそもそも正しくないケース）、このイベントは発火しない！

３．認証イベント成功と失敗時にDB更新追加
　　ログイン失敗回数とアカウントロックまわりを更新している

・管理者用画面の追加
１．applicationContext-security.xml：
　　<sec:intercept-url pattern="/admin/**" access="hasRole('ROLE_ADMIN')" />
　　adminから始まるURLパターンは管理者のみアクセス可能とする

２．JSP側：
　　<sec:authorize access="hasRole('ROLE_ADMIN')">
　　上記タグを使用することで、ロールごとに表示制御可能

３．認可エラーハンドリング
　　AccessDeniedHandlerを継承したクラスを使用することでハンドリング可能。
　　applicationContext-security.xmlにてBean定義した継承クラスを以下のように設定することで認可エラーをハンドリングできる
　　<sec:access-denied-handler ref="accessDeniedHandler" />

・アカウント有効期限切れ機能の追加
１．テーブルにアカウント有効期限切れ日時を用意する

２．UserDetailsのisAccountNonExpiredメソッドをオーバーライドする
　　アカウント有効期限切れ日時と現在日付を比較し、期限が過ぎている場合はfalseを返却するようにすると、
　　Spring Securityが認証時に自動で上記メソッドを参照して、アカウント有効期限切れならAccountExpiredExceptionをスローする
　　あとは上記例外に対応したメッセージが自動で返却される

・パスワード有効期限切れ機能の追加
１．テーブルにパスワード有効期限切れ日時を用意する

２．UserDetailsのisCredentialsNonExpiredメソッドをオーバーライドする
　　パスワード有効期限切れ日時と現在日付を比較し、期限が過ぎている場合はfalseを返却するようにすると、
　　Spring Securityが認証時に自動で上記メソッドを参照して、パスワード有効期限切れならCredentialsExpiredExceptionをスローする
　　あとは上記例外に対応したメッセージが自動で返却される

３．パスワード有効期限切れ事前通知機能
　　ログイン後のリダイレクト先コントローラーにて
　　認証後のユーザー＞パスワード有効期限切れ日時を取得し、現在日時と比較して
　　パスワード有効期限切れ日事前通知の閾値の範囲の場合はメッセージ表示

・プロパティファイルからの値取得
①パスワード有効期限切れ事前通知と②ログイン失敗回数の許容閾値を定数ではなくプロパティファイルに移動
①はWebアプリケーションコンテキスト配下で管理しているBean、②はルートアプリケーションコンテキスト配下で管理しているBeanのため
以下のBean定義をそれぞれのコンテキストファイルに定義する
--------------------------------------------------------------------------
<bean id="propertyConfigurer" class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
    <property name="locations">
        <list>
            <value>classpath:auth.properties</value>
        </list>
    </property>
</bean>
--------------------------------------------------------------------------
使用したいクラスにて、@Valueでプロパティ値をフィールドにマッピングする

・Springコンテナ内のすべてのシングルトンBeanが初期化された後に、追加の初期化処理を行いたいとき
SmartInitializingSingletonを使用する
上記を実装したクラスをDIコンテナで管理して、afterSingletonsInstantiatedメソッドをオーバーライドして
初期化したい処理を記載することができる
以下のような用途で使用する
⇒アプリ起動時に マスターデータを一括ロードして保持したい
⇒全ての@Autowiredや@PostConstructが終わったあとに処理したい
⇒他のBeanへの依存関係がすべて確立されたあとで動作させたい

・二重ログイン防止機能について
同一ログインユーザーによる二重ログインを防ぐ
以下の手順で実装する
⇒applicationContext-security.xml
　　①SessionRegistryImplをBean登録
　　②以下のセッション管理設定を用意
 	<sec:session-management>
		<sec:concurrency-control 
		max-sessions="1" 
		error-if-maximum-exceeded="true"
		session-registry-ref="sessionRegistry" />
	</sec:session-management>
　⇒web.xml
　　①Spring Securityにセッション情報を通知するリスナー追加（HttpSessionEventPublisher）
　⇒ExtendedUser.java
　　①比較判定を行うメソッドをオーバーライド
　⇒messages_ja.properties
　　①以下のキーを追加
　　　　ConcurrentSessionControlAuthenticationStrategy.exceededAllowed

・セッションタイムアウト管理について
セッションタイムアウトを以下の手順でハンドリングする
⇒applicationContext-security.xml
　　①invalid-session-url="/session-invalid"を追加する
　　　　上記は無効なセッションでアクセスされた場合のリダイレクトパスを設定できる
　　②<sec:intercept-url pattern="/session-invalid" access="permitAll()" />
　　　　①のリダイレクト先を認証不要にしないと/loginにリダイレクトされてしまう
　　③ログアウト設定の属性に「delete-cookies="JSESSIONID"」を追加
　　　　上記を追加しないと、ログアウト時に無効なセッションとみなされて/session-invalidにリダイレクトされた
⇒SessionController.java
　　リダイレクト先の遷移ハンドラ追加しただけ
⇒web.xml
　　「<session-timeout>30</session-timeout>」のようにセッションタイムアウトの値を操作すれば動作確認可能

・ここにきてわかったこと
⇒OAuth2を使用してGoogleなどのプロバイダと連携したい場合、XMLベースだと厳しいってことがわかった
⇒JavaConfigベースを推奨しているっぽい

・追記
⇒JavaConfigベースでGoogle OAuth連携成功