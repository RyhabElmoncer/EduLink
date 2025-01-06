package com.EduLink.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  @PostMapping("/register")
  public RedirectView register(
          @ModelAttribute RegisterRequest request,
          RedirectAttributes redirectAttributes
  ) {
    service.signup(request); // Inscription réussie
    redirectAttributes.addFlashAttribute("successMessage", "Inscription réussie ! Connectez-vous.");
    return new RedirectView("/login");
  }




  @PostMapping("/authenticate")
  public RedirectView authenticate(
          @RequestParam("email") String email,
          @RequestParam("password") String password,
          HttpServletRequest request,
          RedirectAttributes redirectAttributes
  ) {
    AuthenticationRequest authRequest = new AuthenticationRequest(email, password);
    try {
      AuthenticationResponse response = service.authenticate(authRequest);

      // Si l'authentification réussit, stocker les informations utilisateur dans la session
      request.getSession().setAttribute("user", response);

      // Redirection vers la page d'accueil
      return new RedirectView("/home");
    } catch (Exception e) {
      // Ajouter un message d'erreur comme attribut flash
      redirectAttributes.addFlashAttribute("errorMessage", "Email ou mot de passe invalide.");

      // Redirection vers la page de connexion
      return new RedirectView("/login");
    }
  }



  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}
