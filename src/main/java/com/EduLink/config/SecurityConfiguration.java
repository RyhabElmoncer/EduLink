package com.venta.projet.config;

import lombok.RequiredArgsConstructor;
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

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
                                .requestMatchers("/api/v1/auth/**").permitAll() // Permet l'accès non authentifié pour l'authentification
                                .requestMatchers("/api/v1/auth/user/role/CLIENT").hasRole("CLIENT") // Restriction de rôle
                                .requestMatchers("/api/v1/auth/user/payerTaxLivreur").permitAll() // Accès libre pour certaines routes
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
config.setAllowedOrigins(List.of("http://51.68.119.170:8888", "http://venta.com.tn/","http://venta.tn/", "http://localhost:57787","http://localhost:4200/"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "*")); // En-têtes autorisés
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Méthodes HTTP autorisées

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Applique cette configuration à toutes les routes
        return source;
    }
}
