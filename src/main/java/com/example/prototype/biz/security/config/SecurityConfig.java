package com.example.prototype.biz.security.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

import com.example.prototype.biz.debugger.OAuth2RedirectDebugger;
import com.example.prototype.biz.security.handler.CustomAuthenticationSuccessHandler;
import com.example.prototype.biz.users.entity.ExtendedUser;
import com.example.prototype.biz.users.listener.ExtendedAccessDeniedHandler;
import com.example.prototype.biz.users.service.AuthenticationUserService;
import com.example.prototype.biz.users.service.UsersService;
import com.example.prototype.biz.utils.MessageUtil;
import com.example.prototype.common.constants.Constants;

/**
 * Spring Security設定クラス
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /** Google OAuth2連携用クライアントID */
    @Value("${google.client-id}")
    private String clientId;

    /** Google OAuth2連携用シークレットキー */
    @Value("${google.client-secret}")
    private String clientSecret;

    /** Google OAuth2連携用リダイレクトURI */
    @Value("${google.redirect-uri}")
    private String redirectUri;

    @Autowired
    private UsersService userService;
    
    @Autowired
    private MessageUtil messageUtil;

    /** OAuth2連携成功後処理Bean */
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    /** 認証用Bean */
    @Bean
    public UserDetailsService userDetailsService() {
        return new AuthenticationUserService();
    }

    /** パスワードエンコーダー用Bean */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** 認可エラー時のハンドリング用Bean */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ExtendedAccessDeniedHandler();
    }

    /** 同一ユーザーの同時ログイン数 制限用Bean */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /** Spring Security管理対象外の設定用Bean */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/resources/**") // 静的リソース（CSS, JS, 画像など）へのセキュリティ除外
        );
    }
    
    /** フォーム認証に使用する情報の定義用Bean */
    @Bean
    public AuthenticationManager authenticationManager(
            MessageSource messageSource,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        // 認証に使用するBeanを設定
        var provider = new DaoAuthenticationProvider();
        provider.setMessageSource(messageSource);
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

    /** OAuth2連携のプロバイダ情報設定用Bean */
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        // Googleログイン用
        ClientRegistration googleLoginRegistration = googleLoginRegistration();
        // Google紐づけ用
        ClientRegistration googleLinkRegistration = googleLinkRegistration();
        return new InMemoryClientRegistrationRepository(googleLoginRegistration, googleLinkRegistration);
    }

    /** OAuth2連携の認証済み情報保持用のBean */
    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    /** OAuth2連携時のクライアント設定用Bean */
    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        // アクセストークンレスポンスのJSONを解析するためのMessageConverterを設定
        var restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(),
                new OAuth2AccessTokenResponseHttpMessageConverter()
            ));

        // カスタムRestTemplateをDefaultAuthorizationCodeTokenResponseClientにセット 
        var client = new DefaultAuthorizationCodeTokenResponseClient();
        client.setRestOperations(restTemplate);

        return client;
    }
    
    /** セキュリティ関連の設定用Bean */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // OAuth2リダイレクト時のデバッグ用
                .addFilterBefore(new OAuth2RedirectDebugger(), OAuth2LoginAuthenticationFilter.class)
                // HTTPリクエストの認可ルール
                .authorizeHttpRequests(authz -> authz
                        // 認証不要のURL
                        .requestMatchers(
                                new AntPathRequestMatcher("/login/**"),
                                new AntPathRequestMatcher("/oauth2/**"),
                                new AntPathRequestMatcher("/session-invalid"),
                                new AntPathRequestMatcher("/logout/**"))
                        .permitAll()

                        // ロールベースの認可設定
                        .requestMatchers(new AntPathRequestMatcher("/user/**"))
                        .hasAnyRole("USER", "ADMIN", "DEVELOPER") // ※Spring Securityが内部で「ROLE_」を追加する
                        .requestMatchers(new AntPathRequestMatcher("/admin/**"))
                        .hasAnyRole("ADMIN", "DEVELOPER")
                        // その他のリクエストは認証必須
                        .anyRequest().authenticated())

                // フォームベースの認証設定
                .formLogin(login -> login
                        .loginPage("/login") // ログイン画面表示用のURL（独自のJSP）
                        .loginProcessingUrl("/login") // 認証処理を行うPOST先
                        .usernameParameter("loginId") // フォームの name="loginId" を認識
                        .passwordParameter("password") // フォームの name="password" を認識
                        .defaultSuccessUrl("/", true) // ログイン成功後の遷移先
                        .failureUrl("/login?error")) // ログイン失敗時の遷移先

                // OAuth 2.0の連携設定
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // OAuth2ログイン画面表示用のURL
                        .clientRegistrationRepository(clientRegistrationRepository()) // プロバイダ情報をSpring Securityに渡す
                        .authorizedClientService(authorizedClientService(clientRegistrationRepository())) // 認証済み情報を設定
                        .tokenEndpoint(token -> token
                                .accessTokenResponseClient(accessTokenResponseClient()) // アクセストークン取得方法の設定
                        )
                        .successHandler(customAuthenticationSuccessHandler)) // 成功イベント

                // ログアウト設定
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout=true") // ログアウト成功時のURL
                        .invalidateHttpSession(true) // ログアウト時にサーバー側のセッション破棄
                        .deleteCookies("JSESSIONID")) // ログアウト時にクライアント側のクッキー削除

                // 認証/認可エラーハンドリング設定
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler())) // 認可エラーハンドリング

                // セッション管理設定
                .sessionManagement(session -> session
                        .invalidSessionUrl("/session-invalid") // 無効なセッションでアクセスされた場合のリダイレクトパス
                        .maximumSessions(1) // 最大セッション数
                        .maxSessionsPreventsLogin(true) // 最大セッション数を超えた場合にログインを拒否するかどうか（true: 新しいログインを拒否/ false: 古いセッションを破棄して新規ログインを許可）
                        .sessionRegistry(sessionRegistry()));

        return http.build();
    }
    
    /**
     * OAuth2連携時のユーザー情報取得処理用Bean
     * @return
     */
    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> customOidcUserService() {
        return userRequest -> {
            // OAuth2連携後のレスポンス取得
            OidcUser oidcUser = new OidcUserService().loadUser(userRequest);
            
            ExtendedUser user = null;
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            if (Constants.GOOGLE_REGISTRATION_ID_LINK.equals(registrationId)) {
                // Google OAuth2連携の紐づけ処理
                user = (ExtendedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                int count = userService.findCountByEmail(oidcUser.getEmail());
                if (count > 0) {
                    // Googleアカウント紐づけ処理
                    user.setGoogleSub(oidcUser.getSubject());
                    user.setGoogleLinked(true);
                    userService.updateGoogleSub(user);
                    // 認証情報を再取得
                    user = userService.findByLoginIdForAuth(user.getLoginId());
                }
            } else if (Constants.GOOGLE_REGISTRATION_ID_LOGIN.equals(registrationId)) {
                // Google OAuth2連携のログイン処理
                user = userService.findByGoogleSub(oidcUser.getSubject());
                if (user == null) {
                    // Googleアカウント認証失敗
                    throw new UsernameNotFoundException(messageUtil.getMessage(Constants.ERR_MSG_GOOGLE_OAUTH_FAILURE));
                }
            }

            return user;
        };
    }
    
    /**
     * Google OAuth2ログイン用プロバイダ設定取得
     * @return
     */
    private ClientRegistration googleLoginRegistration() {
        // Googleプロバイダ設定（ログイン用）
        return googleRegistration(Constants.GOOGLE_REGISTRATION_ID_LOGIN);
    }

    /**
     * Google OAuth2紐づけ用プロバイダ設定取得
     * @return
     */
    private ClientRegistration googleLinkRegistration() {
        // Googleプロバイダ設定（紐づけ用）
        return googleRegistration(Constants.GOOGLE_REGISTRATION_ID_LINK);
    }
    
    /**
     * Google OAuth2用プロバイダ取得
     * @param registrationId
     * @return
     */
    private ClientRegistration googleRegistration(String registrationId) {
        return CommonOAuth2Provider.GOOGLE
                .getBuilder(registrationId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo") // ← 推奨URI
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .userNameAttributeName("sub")
                .clientName("Google")
                .build();
    }
}
