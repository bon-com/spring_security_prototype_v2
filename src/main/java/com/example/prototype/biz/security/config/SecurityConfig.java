package com.example.prototype.biz.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.prototype.biz.users.listener.ExtendedAccessDeniedHandler;
import com.example.prototype.biz.users.service.AuthenticationUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new AuthenticationUserService(); // 認証クラス
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // パスワードエンコーダー
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ExtendedAccessDeniedHandler(); // 認可エラー時のハンドリングクラス
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl(); // 同一ユーザーの同時ログイン数を制限するためのクラス
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                // 認証不要のURL
                .requestMatchers(
                        new AntPathRequestMatcher("/login"),
                        new AntPathRequestMatcher("/oauth2/**"),
                        new AntPathRequestMatcher("/session-invalid"))
                .permitAll()
                
                // ロールベースの認可設定
                .requestMatchers(new AntPathRequestMatcher("/user/**"))
                .hasAnyRole("USER", "ADMIN", "DEVELOPER") // ※Spring Securityが内部で「ROLE_」を追加する
                .requestMatchers(new AntPathRequestMatcher("/admin/**"))
                .hasAnyRole("ADMIN", "DEVELOPER")
                // その他のリクエストは認証必須
                .anyRequest().authenticated())

                .formLogin(login -> login
                        .loginPage("/login") // ログイン画面のURL（独自のJSP）
                        .loginProcessingUrl("/login") // 認証処理を行うPOST先
                        .usernameParameter("loginId") // フォームの name="loginId" を認識
                        .passwordParameter("password") // フォームの name="password" を認識
                        .defaultSuccessUrl("/", true) // ログイン成功後の遷移先
                        .failureUrl("/login?error") // ログイン失敗時の遷移先
                        .permitAll())

                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout=true") // ログアウト成功時のURL
                        .invalidateHttpSession(true) // ログアウト時にサーバー側のセッション破棄
                        .deleteCookies("JSESSIONID") // ログアウト時にクライアント側のクッキー削除
                        .permitAll())

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler())) // アクセス拒否時のハンドリングクラス

                .sessionManagement(session -> session
                        .invalidSessionUrl("/session-invalid") // 無効なセッションでアクセスされた場合のリダイレクトパス
                        .maximumSessions(1) // 最大セッション数
                        .maxSessionsPreventsLogin(true) // 最大セッション数を超えた場合にログインを拒否するかどうか（true: 新しいログインを拒否/ false: 古いセッションを破棄して新規ログインを許可）
                        .sessionRegistry(sessionRegistry()));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/resources/**") // 静的リソース（CSS, JS, 画像など）へのセキュリティ除外
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        // 認証プロバイダ設定
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

}
