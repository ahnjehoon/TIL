- 둘다 웹 어플리케이션에서 요청을 가로채고 처리하는데 사용함
- 다음과 같은 차이점이 존재함
- 실행시점이 DispatcherServlet 전(Filter)과 후(Interceptor)로 나뉨
- 동작 위치
	- Filter: Java Servlet 영역이여서 Spring Context 외부에서 동작
	- Interceptor: Spring Context 내부에서 동작하여 모든 빈에 접근 가능
- 용도
	- Filter: 인코딩 변환, XSS 방어 등 앱 전역적으로 처리해야 하는 로직
	- Interceptor: 세부 보안 및 인증/인가, API 호출에 대한 로깅 등에 사용

## Interceptor
- 구현 방법
	1. HandlerInterceptor 인터페이스 구현
	2. XML 또는 Java 설정에 Interceptor 등록