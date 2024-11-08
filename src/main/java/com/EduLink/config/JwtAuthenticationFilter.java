package com.venta.projet.config;

import com.venta.projet.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;


  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    if (request.getServletPath().contains("/api/v1/auth")) {
      filterChain.doFilter(request, response);
      return;
    }

    // Récupérer le header Authorization
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;

    // Vérifier que le header Authorization est présent et commence par "Bearer "
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    // Extraire le JWT du header Authorization
    jwt = authHeader.substring(7);
    // Extraire le nom d'utilisateur du JWT
    userEmail = jwtService.extractUsername(jwt);

    // Si un nom d'utilisateur a été extrait et que le contexte de sécurité n'est pas encore authentifié
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // Charger les détails de l'utilisateur par son nom d'utilisateur
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

      // Vérifier si le token est valide (non expiré et non révoqué)
      var isTokenValid = tokenRepository.findByToken(jwt)
              .map(t -> !t.isExpired() && !t.isRevoked())
              .orElse(false);

      // Si le JWT et les détails de l'utilisateur sont valides
      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        // Créer un token d'authentification
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        // Définir les détails de l'authentification à partir de la requête
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // Mettre à jour le contexte de sécurité avec le token d'authentification
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    // Continuer la chaîne de filtres
    filterChain.doFilter(request, response);
  }
}
