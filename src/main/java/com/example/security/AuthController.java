package com.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final GoogleAuthService googleAuthService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository resetTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        String token = jwtUtil.generateToken(userDetails);
        var user = userRepository.findByUsername(request.username()).orElseThrow();
        return ResponseEntity.ok(new LoginResponse(token, user.getEmail(), user.getDisplayName(), user.getRole()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El nombre de usuario ya está en uso"));
        }
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body(Map.of("message", "El correo electrónico ya está registrado"));
        }

        var user = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .displayName(request.displayName() != null ? request.displayName() : request.username())
                .password(passwordEncoder.encode(request.password()))
                .role("USER")
                .build();
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(token, user.getEmail(), user.getDisplayName(), user.getRole()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El correo es requerido"));
        }

        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // No revelamos si el email existe o no por seguridad
            return ResponseEntity.ok(Map.of("message", "Si el correo está registrado, recibirás un enlace para restablecer tu contraseña"));
        }

        // Invalidar tokens anteriores
        resetTokenRepository.deleteByEmail(email);

        // Generar nuevo token
        String resetToken = UUID.randomUUID().toString();
        var tokenEntity = PasswordResetTokenEntity.builder()
                .email(email)
                .token(resetToken)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .used(false)
                .build();
        resetTokenRepository.save(tokenEntity);

        // En desarrollo devolvemos el token directamente
        // En producción se enviaría por email
        return ResponseEntity.ok(Map.of(
            "message", "Si el correo está registrado, recibirás un enlace para restablecer tu contraseña",
            "resetToken", resetToken,
            "resetUrl", "https://vault-vintage-frontend.vercel.app/reset-password?token=" + resetToken
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (request.token() == null || request.token().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Token requerido"));
        }
        if (request.newPassword() == null || request.newPassword().length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("message", "La contraseña debe tener al menos 6 caracteres"));
        }

        var tokenOpt = resetTokenRepository.findByToken(request.token());
        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Token inválido"));
        }

        var tokenEntity = tokenOpt.get();
        if (tokenEntity.isUsed()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Este token ya ha sido usado"));
        }
        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("message", "El token ha expirado"));
        }

        var userOpt = userRepository.findByEmail(tokenEntity.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Usuario no encontrado"));
        }

        var user = userOpt.get();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        tokenEntity.setUsed(true);
        resetTokenRepository.save(tokenEntity);

        return ResponseEntity.ok(Map.of("message", "Contraseña restablecida exitosamente"));
    }

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleRequest request) {
        return ResponseEntity.ok(googleAuthService.authenticate(request.credential()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfo> me() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(401).build();
        }
        var username = auth.getName();
        var user = userRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(new UserInfo(user.getUsername(), user.getEmail(), user.getDisplayName(), user.getRole()));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserInfo>> users() {
        var users = userRepository.findAll().stream()
                .map(u -> new UserInfo(u.getUsername(), u.getEmail(), u.getDisplayName(), u.getRole()))
                .toList();
        return ResponseEntity.ok(users);
    }

    public record LoginRequest(String username, String password) {}
    public record GoogleRequest(String credential) {}
    public record LoginResponse(String token, String email, String name, String role) {}
    public record UserInfo(String username, String email, String displayName, String role) {}
    public record RegisterRequest(String username, String email, String password, String displayName) {}
    public record ResetPasswordRequest(String token, String newPassword) {}
}
