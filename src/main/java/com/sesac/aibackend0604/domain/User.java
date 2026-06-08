package com.sesac.aibackend0604.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 로그인 아이디 (유일)
     */
    @Column(
            unique = true,
            nullable = false,
            length = 100
    )
    private String username;

    /**
     * BCrypt 해시 (Day4에서 사용)
     */
    @Column(length = 200)
    private String passwordHash;

    /**
     * USER / ADMIN
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /** 소셜 로그인 제공자 (ex. "GOOGLE"). 일반 회원은 null. */
    @Column(length = 20)
    private String provider;

    /** 제공자가 발급한 불변 고유 ID (OIDC sub). 일반 회원은 null. */
    @Column(length = 200)
    private String providerId;

    /** 구글 OAuth2 신규 가입 시 사용하는 팩토리 메서드. */
    public static User oauthUser(String email, String providerId) {
        return User.builder()
                .username(email)
                .role(Role.USER)
                .provider("GOOGLE")
                .providerId(providerId)
                .build();
    }

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ChatLog> chatLogs = new ArrayList<>();
}