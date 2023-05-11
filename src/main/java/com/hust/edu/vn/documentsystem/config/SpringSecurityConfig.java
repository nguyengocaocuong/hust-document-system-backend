package com.hust.edu.vn.documentsystem.config;


public class SpringSecurityConfig {
//    private final JwtAthFilter jwtAthFilter;
//    private static final String[] WHITE_LIST_URLS = {
//            "/api/v1/authentication/**",
//            "/api/v1/public/**",
//            "/swagger-ui/**",
//            "/v3/api-docs/**"
//    };
//
//    private final CustomAuthenticationProvider customAuthenticationProvider;
//
//    public SpringSecurityConfig(JwtAthFilter jwtAthFilter, CustomAuthenticationProvider customAuthenticationProvider) {
//        this.jwtAthFilter = jwtAthFilter;
//        this.customAuthenticationProvider = customAuthenticationProvider;
//    }
//
//
//
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors()
//                .configurationSource(new CorsConfigurationSource() {
//                    @Override
//                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                        CorsConfiguration corsConfiguration = new CorsConfiguration();
//                        corsConfiguration.setAllowedOrigins(Arrays.asList("FRONTEND_URL","https://view.officeapps.live.com/op/embed.aspx"));
//                        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
//                        corsConfiguration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
//                        corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
//                        corsConfiguration.setAllowCredentials(false);
//                        corsConfiguration.setMaxAge(Duration.ofMinutes(20));
//                        return corsConfiguration;
//                    }
//                })
//                .and()
//                .csrf()
//                .disable();
//
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        /* config public api*/
//        http.authorizeHttpRequests()
//                .requestMatchers(WHITE_LIST_URLS)
//                .permitAll();
//
//        http.authorizeHttpRequests()
//                .requestMatchers("/api/v1/admins/**","/api/v1/socket/admins/**")
//                .hasAuthority("ADMIN");
//
//        http.authorizeHttpRequests()
//                .requestMatchers("/api/v1/users/**","/api/v1/socket/users/**")
//                .hasAuthority("USER");
//
//        http.authorizeHttpRequests()
//                .requestMatchers("/api/v1/generals/**")
//                .hasAnyAuthority("USER", "ADMIN");
//
//
//        http.addFilterBefore(jwtAthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        http.httpBasic();
//
//        return http.build();
//
//    }
//
//    @Autowired
//    public void bindAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
//        authenticationManagerBuilder
//                .authenticationProvider(customAuthenticationProvider);
//    }
//
//
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }

}
