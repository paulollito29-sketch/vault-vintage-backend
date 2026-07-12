package com.example.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    public AuthController.LoginResponse authenticate(String idToken) {
        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(TOKEN_INFO_URL + idToken))
                    .GET()
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Invalid Google token");
            }

            var json = objectMapper.readTree(response.body());
            var email = json.get("email").asText();
            var name = json.has("name") ? json.get("name").asText() : email;
            var sub = json.get("sub").asText();

            var user = userRepository.findByGoogleSub(sub)
                    .orElseGet(() -> createGoogleUser(email, name, sub));

            var userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(),
                    java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole())));

            var token = jwtUtil.generateToken(userDetails);
            return new AuthController.LoginResponse(token, user.getEmail(), user.getDisplayName(), user.getRole());

        } catch (Exception e) {
            throw new RuntimeException("Google authentication failed: " + e.getMessage());
        }
    }

    private UserEntity createGoogleUser(String email, String name, String sub) {
        if (userRepository.existsByEmail(email)) {
            var existing = userRepository.findByEmail(email).orElseThrow();
            existing.setGoogleSub(sub);
            return userRepository.save(existing);
        }

        var user = UserEntity.builder()
                .username(email)
                .email(email)
                .displayName(name)
                .googleSub(sub)
                .password(passwordEncoder.encode(java.util.UUID.randomUUID().toString()))
                .role("USER")
                .build();
        return userRepository.save(user);
    }
}
