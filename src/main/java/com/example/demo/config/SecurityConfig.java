package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuração de segurança com Microsoft Entra ID (Azure AD)
 * Suporta autenticação JWT via OAuth2 Resource Server
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${app.cors.allowed-origins:http://localhost:19006,http://localhost:8081}")
    private String allowedOrigins;

    @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${app.cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${app.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configurando Security Filter Chain");

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> 
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers(
                                "/",
                                "/api/health",
                                "/actuator/**",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // Endpoints protegidos - comentado temporariamente para desenvolvimento
                        // .requestMatchers("/api/**").authenticated()
                        // TODO: Descomentar para produção
                        .anyRequest().permitAll()
                );
                // OAuth2 JWT desabilitado temporariamente para desenvolvimento sem Azure
                // Descomente quando configurar o Azure AD
                //.oauth2ResourceServer(oauth2 -> 
                //        oauth2.jwt(jwt -> log.info("JWT Resource Server configurado"))
                //);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Configurando CORS com origins: {}", allowedOrigins);

        CorsConfiguration configuration = new CorsConfiguration();
        
        // Configuração de origens
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins);
        
        // Configuração de métodos
        List<String> methods = Arrays.asList(allowedMethods.split(","));
        configuration.setAllowedMethods(methods);
        
        // Configuração de headers
        if ("*".equals(allowedHeaders)) {
            configuration.addAllowedHeader("*");
        } else {
            List<String> headers = Arrays.asList(allowedHeaders.split(","));
            configuration.setAllowedHeaders(headers);
        }
        
        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
