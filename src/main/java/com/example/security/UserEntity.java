package com.example.security;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vv_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String email;

    private String displayName;

    @Column(unique = true)
    private String googleSub;

    @Column(nullable = false)
    private String role;
}
