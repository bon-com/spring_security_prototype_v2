package com.example.prototype.biz.security.config;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig_BKJ {

//    @Value("${google.client-id}")
//    private String clientId;
//
//    @Value("${google.client-secret}")
//    private String clientSecret;
//
//    @Value("${google.redirect-uri}")
//    private String redirectUri;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private ExtendedAccessDeniedHandler accessDeniedHandler;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SessionRegistry sessionRegistry() {
//        return new SessionRegistryImpl();
//    }
//    
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(clientRegistration());
//    }
//
//    @Bean
//    public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository repo) {
//        return new InMemoryOAuth2AuthorizedClientService(repo);
//    }
//    
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(new AntPathRequestMatcher("/resources/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/oauth2/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/session-invalid")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/user/**")).hasAnyRole("USER", "ADMIN", "DEVELOPER")
//                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAnyRole("ADMIN", "DEVELOPER")
//                .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                .loginPage("/login")
//                .defaultSuccessUrl("/")
//                .failureUrl("/login?error")
//                .usernameParameter("loginId")
//                .passwordParameter("password")
//                .permitAll()
//            )
//            .oauth2Login(oauth2 -> oauth2
//                .loginPage("/login")
//                .clientRegistrationRepository(clientRegistrationRepository())
//                .authorizedClientService(authorizedClientService(clientRegistrationRepository()))
//                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService()))
//            )
//            .logout(logout -> logout
//                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                    .logoutSuccessUrl("/login?logout=true")
//                    .invalidateHttpSession(true)
//                    .deleteCookies("JSESSIONID")
//                )
//            .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler))
//            .sessionManagement(session -> session
//                .invalidSessionUrl("/session-invalid")
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(true)
//                .sessionRegistry(sessionRegistry())
//            );
//
//        return http.build();
//    }
//    
//    @Bean
//    public ClientRegistration clientRegistration() {
//        return ClientRegistration.withRegistrationId("google")
//            .clientId(clientId)
//            .clientSecret(clientSecret)
//            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//            .redirectUri(redirectUri)
//            .scope("profile", "email")
//            .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
//            .tokenUri("https://oauth2.googleapis.com/token")
//            .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//            .userNameAttributeName("sub")
//            .clientName("Google")
//            .build();
//    }
//    
//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
//        return userRequest -> {
//            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
//            OAuth2User oAuth2User = delegate.loadUser(userRequest);
//
//            String email = oAuth2User.getAttribute("email");
//
//            // DBとの紐づけや自動登録処理を書く
//            return oAuth2User;
//        };
//    }
//
//    // 静的リソースのセキュリティ除外
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers("/resources/**");
//    }
//
//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//            .userDetailsService(userDetailsService)
//            .passwordEncoder(passwordEncoder())
//            .and()
//            .build();
//    }
}
