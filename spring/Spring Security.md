- 보안에 관련되어 인증과 권한부여를 위한 기능을 제공함
- Spring의 Filter 단계에서 동작하지만 Interceptor 단계에서도 일부 보안 기능을 구현할 수 있음

## 주요 개념
- 인증(Authentication)
	- 사용자의 신원을 확인하는 과정
	- 주로 사용자이름과 비밀번호를 통해 이루어짐
	- 다양한 인증 방식 지원 (폼 기반, HTTP 기본 인증, OAuth 2.0 등)
- 권한 부여(Authorization)
	- 인증된 사용자에게 특정 리소스에 대한 접근 권한을 부여하는 과정
	- 역할(Role) 기반 접근 제어(Role-Based Access Control) 지원
- Filter Chain
	요청을 가로채고 보안 로직을 적용하는 필터들의 연쇄
	각 필터는 특정 보안 기능을 담당함 (예: CSRF 방지, 세션 관리 등)

## 주요 컴포넌트
- SecurityContextHolder
	- 현재 보안 컨텍스트에 대한 세부 정보를 저장
	- 인증된 사용자의 정보를 애플리케이션 어디서나 접근 가능하게 함
- UserDetailsService
	- 사용자 정보를 로드하는 핵심 인터페이스
	- 데이터베이스나 다른 저장소에서 사용자 정보를 조회하는 로직 구현
- PasswordEncoder
	- 비밀번호를 안전하게 해시하고 검증하는 인터페이스
	- BCrypt, SCrypt, Argon2 등 다양한 해시 알고리즘 지원