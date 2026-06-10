package com.sesac.aibackend0604.security;

import com.sesac.aibackend0604.domain.User;
import com.sesac.aibackend0604.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 카카오 OAuth2 로그인 성공 핸들러.
 * 카카오는 OIDC 설정하지 않으면 principal이 OAuth2User(DefaultOAuth2User)로 옵니다.
 * user-name-attribute: id 설정 덕분에 getName()이 카카오 숫자 ID를 반환합니다.
 * 이메일 scope를 요청하지 않았으므로 username은 "kakao_{id}" 형태로 생성합니다.
 */
@Component
@RequiredArgsConstructor
public class KakaoOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.oauth2.redirect-uri:http://localhost:5173/oauth/callback}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        // user-name-attribute: id → getName()이 카카오 고유 숫자 ID(String)를 반환
        String providerId = oauth2User.getName();
        String username = "kakao_" + providerId;

        User user = userRepository.findByProviderAndProviderId("KAKAO", providerId)
                .orElseGet(() -> userRepository.save(User.oauthUser(username, "KAKAO", providerId)));

        String token = jwtUtil.generate(user.getUsername(), user.getRole().name());
        response.sendRedirect(redirectUri + "?token=" + token);
    }
}
