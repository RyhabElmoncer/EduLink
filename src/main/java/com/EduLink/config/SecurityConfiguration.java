package com.EduLink.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    // URLs accessibles sans authentification
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/authenticate",
            "/api/v1/auth/register" ,


    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configuration CORS globale
                .csrf(AbstractHttpConfigurer::disable) // Désactive CSRF, non nécessaire pour les API stateless
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(WHITE_LIST_URL).permitAll() // Permet l'accès non authentifié pour les URLs dans la liste blanche

                                .anyRequest().authenticated() // Toute autre requête nécessite une authentification
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) // Sessions stateless (JWT)
                .authenticationProvider(authenticationProvider) // Utilisation de votre provider d'authentification
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Filtre JWT
                .logout(logout ->
                        logout
                                .logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler) // Gestionnaire de déconnexion personnalisé
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()) // Nettoyage du contexte
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(""));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "*")); // En-têtes autorisés
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Méthodes HTTP autorisées

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Applique cette configuration à toutes les routes
        return source;
    }
}
