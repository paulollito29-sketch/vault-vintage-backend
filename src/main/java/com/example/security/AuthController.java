package com.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final GoogleAuthService googleAuthService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        String token = jwtUtil.generateToken(userDetails);
        var user = userRepository.findByUsername(request.username()).orElseThrow();
        return ResponseEntity.ok(new LoginResponse(token, user.getEmail(), user.getDisplayName(), user.getRole()));
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
}
