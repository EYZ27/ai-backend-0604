# Kakao OAuth2 연결 가이드
## OIDC(OpenID Connect) 미사용 방식
### 1. application.yaml
(1) Kakao 클라이언트/프로바이더 등록
```yaml
kakao:
    client-id: ${KAKAO_CLIENT_ID}
    client-secret: ${KAKAO_CLIENT_SECRET}
    client-authentication-method: client_secret_post   # ← Kakao 필수
    redirect-uri: "{baseUrl}/login/oauth2/code/kakao"
    authorization-grant-type: authorization_code
    scope: profile_nickname

provider:
    kakao:
        authorization-uri: https://kauth.kakao.com/oauth/authorize
        token-uri: https://kauth.kakao.com/oauth/token
        user-info-uri: https://kapi.kakao.com/v2/user/me
        user-name-attribute: id
```
- Kakao는 Spring Security 기본 인증 방식(client_secret_basic)을 지원하지 않아 client_secret_post로 명시.
- Kakao는 Spring 프리셋이 없는 provider라 authorization-uri/token-uri/user-info-uri를 직접 지정.

### 2. KakaoOAuth2LoginSuccessHandler 
(1) 카카오용 핸들러 신규 작성으로 처리함
- 구글 OIDC 방식과 별도로 처리하기 위해 신규 핸들러 작성
- OidcUser가 아닌 OAuth2User로 오는 것을 처리하기 위함
- user-name-attribute: id 설정 덕분에 getName()이 Kakao 고유 숫자 ID 반환.
- 이메일 scope가 없으므로 username = "kakao_{id}"로 생성.

### 3. User.oauthUser()
(1) User 시그니처 변경
- oauthUser(email, providerId) (provider="GOOGLE" 하드코딩)
  → oauthUser(username, provider, providerId) 로 일반화 — Google/Kakao 공용으로 사용.

### 4. SecurityConfig
(1) provider별 분기 + 에러 처리
- oauth2Login.successHandler에서 registrationId("kakao"/"google")를 보고 알맞은 핸들러로 위임.
- failureHandler 추가: 인증 실패 시 /login GET 리다이렉트(→ 405) 대신 JSON 401 반환. (google과 다름)
- permitAll 경로에 /, /css/**, /js/**, /*.png 등 추가 (정적 리소스/홈 접근 허용).

## OIDC(OpenID Connect) 사용 방식

현재 설정(profile_nickname만 동의, 이메일 미동의)을 기준으로 정리.

### 0. 사전 준비
- Kakao Developers 콘솔 > 제품 설정 > 카카오 로그인 > OpenID Connect 활성화
- 활성화하지 않으면 scope에 openid를 넣어도 ID Token이 발급되지 않음

### 1. application.yaml 변경점

(1) registration.kakao - scope에 openid 추가
```yaml
kakao:
  client-id: ${KAKAO_CLIENT_ID}
  client-secret: ${KAKAO_CLIENT_SECRET}
  client-authentication-method: client_secret_post
  redirect-uri: "{baseUrl}/login/oauth2/code/kakao"
  authorization-grant-type: authorization_code
  client-name: Kakao
  scope:
    - openid
    - profile_nickname
```

(2) provider.kakao - jwk-set-uri 추가, user-name-attribute 변경
```yaml
provider:
  kakao:
    authorization-uri: https://kauth.kakao.com/oauth/authorize
    token-uri: https://kauth.kakao.com/oauth/token
    user-info-uri: https://kapi.kakao.com/v2/user/me
    jwk-set-uri: https://kauth.kakao.com/.well-known/jwks.json
    user-name-attribute: sub
```
- jwk-set-uri가 없으면 ID Token(JWT) 서명을 검증할 공개키를 못 구해 인증 실패
- user-name-attribute를 기존 id → sub로 변경 (OIDC 표준 클레임명, 값은 기존 id와 동일한 카카오 회원번호)

### 2. 코드 변경점

대상 파일: `/security/KakaoOAuth2LoginSuccessHandler.java`

(1) import 변경
```java
// 제거
import org.springframework.security.oauth2.core.user.OAuth2User;

// 추가
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
```

(2) onAuthenticationSuccess 내부 - principal 캐스팅 및 providerId 추출 변경
```java
// 변경 전
OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
String providerId = oauth2User.getName();
String username = "kakao_" + providerId;

// 변경 후
OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
String providerId = oidcUser.getSubject(); // user-name-attribute: sub 와 동일한 값
String username = "kakao_" + providerId;
```

- 나머지 로직(`findByProviderAndProviderId`, `User.oauthUser(...)`, JWT 발급, redirect)은 그대로 유지
- email scope를 받지 않았으므로 `oidcUser.getEmail()`은 null → username을 email 기반이 아닌 `"kakao_" + providerId`로 생성하는 현재 방식을 유지해야 함

`/security/SecurityConfig.java`는 수정 불필요
- success/failure 핸들러의 registrationId("kakao"/"google") 분기 로직은 principal 타입과 무관하게 동작 → 그대로 유지
- Spring Security는 ClientRegistration의 scope에 openid가 포함되면 자동으로 OidcUserService를 사용해 OidcUser를 생성함 (별도 userService 등록 코드 불필요)
- 따라서 변경 범위는 application.yaml + KakaoOAuth2LoginSuccessHandler.java 두 곳뿐

### 3. Google과의 비교
- Google은 Spring 내장 프리셋(CommonOAuth2Provider)이 있어 client-id/secret/scope만으로 충분
- Kakao는 프리셋이 없어 provider.kakao 블록(authorization-uri, token-uri, user-info-uri, jwk-set-uri, user-name-attribute)을 직접 명시해야 함

### 4. 주의사항
- nickname 클레임은 profile_nickname 동의 항목과 별개로 필요 시 추가 확인
- email 클레임은 account_email 동의가 없으면 계속 null
