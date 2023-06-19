package com.hust.edu.vn.documentsystem.config;

import com.hust.edu.vn.documentsystem.service.impl.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    private final JwtAthFilter jwtAthFilter;
    private static final String[] WHITE_LIST_URLS = {
            "/api/v1/authentication/**",
            "/api/v1/public/**",
    };

    private final CustomAuthenticationProvider customAuthenticationProvider;

    public SpringSecurityConfig(JwtAthFilter jwtAthFilter, CustomAuthenticationProvider customAuthenticationProvider) {
        this.jwtAthFilter = jwtAthFilter;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(Arrays.asList(System.getenv("FRONTEND_URL"),"https://view.officeapps.live.com/op/embed.aspx", "http://localhost:3000"));
                    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
                    corsConfiguration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
                    corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
                    corsConfiguration.setAllowCredentials(false);
                    corsConfiguration.setMaxAge(Duration.ofMinutes(20));
                    return corsConfiguration;
                })
                .and()
                .csrf()
                .disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /* config public api*/
        http.authorizeHttpRequests()
                .requestMatchers(WHITE_LIST_URLS)
                .permitAll();

        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/admins/**","/api/v1/socket/admins/**")
                .hasAuthority("ADMIN");

        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/users/**","/api/v1/socket/users/**")
                .hasAuthority("USER");

        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/generals/**")
                .hasAnyAuthority("USER", "ADMIN");


        http.addFilterBefore(jwtAthFilter, UsernamePasswordAuthenticationFilter.class);

        http.httpBasic();

        return http.build();

    }

    @Autowired
    public void bindAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder
                .authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
