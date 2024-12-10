package com.EduLink.auth;

import com.EduLink.Enum.Role;
import com.EduLink.Models.User;
import com.EduLink.config.JwtService;
import com.EduLink.repository.UserRepository;
import com.EduLink.token.Token;
import com.EduLink.token.TokenRepository;
import com.EduLink.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  // @Autowired
  // private NotificationServiceImplementation notificationService;
  @Autowired
  private JavaMailSender emailSender;

  public AuthenticationResponse signup(RegisterRequest request) {
    if (repository.existsByEmail(request.getEmail())) {
      //throw new IllegalArgumentException("L'email est déjà utilisé");
      System.out.println("l email deja exist");
    }

    var user = User.builder()
            .firstName(request.getFirstname())
            .lastName(request.getLastname())
            .email(request.getEmail())

            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())

           // Default status
            .build();
    var savedUser = repository.save(user);

    // Send verification email
    sendVerificationEmail(user);

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  private void sendVerificationEmail(User user) {
    // Generate verification token
    String verificationToken = jwtService.generateToken(user); // Modify as needed for email verification

    String verificationLink = "http://localhost:8088/api/v1/auth/verify-email?token=" + verificationToken;

    String subject = "Verify your email";
    String content = String.format("Dear %s %s,\n\nPlease click the link below to verify your email:\n%s\n\nBest regards,\nYour Team",
            user.getFirstName(), user.getLastName(), verificationLink);

    try {
      MimeMessage message = emailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(user.getEmail());
      helper.setSubject(subject);
      helper.setText(content, true);
      emailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send verification email", e);
    }
  }
  public void forgotPassword(String email) throws MessagingException {
    User user = repository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

    String resetToken = jwtService.generateToken(user);
    String resetLink = "http://localhost:4200/reset-password?token=" + resetToken;

    String subject = "Reset your password";
    String content = String.format("Dear %s %s,\n\nPlease click the link below to reset your password:\n%s\n\nBest regards,\nYour Team",
            user.getFirstName(), user.getLastName(), resetLink);

    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(email);
    helper.setSubject(subject);
    helper.setText(content, true);

    emailSender.send(message);
  }

  public void resetPassword(String token, String newPassword) {
    String userEmail = jwtService.extractUsername(token);
    User user = repository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

    if (jwtService.isTokenValid(token, user)) {
      user.setPassword(passwordEncoder.encode(newPassword));
      repository.save(user);
    } else {
      throw new IllegalArgumentException("Invalid or expired token");
    }
  }

  public AuthenticationResponse authenticateWithFirebase(String firebaseToken) throws FirebaseAuthException {
    if (firebaseToken == null || !firebaseToken.startsWith("Bearer ")) {
      throw new IllegalArgumentException("Invalid Firebase token format");
    }

    String token = firebaseToken.substring(7);

    FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
    String uid = decodedToken.getUid();
    String email = decodedToken.getEmail();
    String fullName = decodedToken.getName();
    String[] nameParts = fullName != null ? fullName.split(" ", 2) : new String[]{"", ""};
    String firstName = nameParts.length > 0 ? nameParts[0] : "";
    String lastName = nameParts.length > 1 ? nameParts[1] : "";

    User user = repository.findByEmail(email).orElseGet(() -> {
      User newUser = new User();
      newUser.setEmail(email);
      newUser.setFirstName(firstName);
      newUser.setLastName(lastName);
      newUser.setPassword(passwordEncoder.encode(uid));
      return repository.save(newUser);
    });

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
      );
      var user = repository.findByEmail(request.getEmail())
              .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.getEmail()));

      /*// Check if the user has the role ADMIN or SOUS_ADMIN
      if (user.getRole() != Role.ADMIN && user.getRole() != Role.SOUS_ADMIN) {
        throw new AccessDeniedException("Access restricted to ADMIN and SOUS_ADMIN roles");
      }*/

      var jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);
      revokeAllUserTokens(user);
      saveUserToken(user, jwtToken);

      if (user.getRole() == Role.STUDENT) {
        // notificationService.fetchAndSendUnreadNotifications(user.getId());
      }

      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)

              .build();
    } catch (Exception e) {
      // Log exception for debugging
      System.err.println("Authentication failed: " + e.getMessage());
      throw e; // Re-throw the exception to ensure the client gets the correct error response
    }
  }

  private void saveUserToken(User user, String jwtToken) {
    Token token = Token.builder()
            .user(user)  // Utilisation de la classe User correctement
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }



  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
