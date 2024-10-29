
## HandlerInterceptor

### preHandle(request, response, handler)
- Controller 실행 전에 호출됨
- 반환값이 true면 다음단계로 진행
- 반환값이 false면 요청 처리 중단
- 주로 인증, 권한, 체크 등에 사용됨
### postHandle(request, response, handler, modelAndView)
- Controller 실행 후 뷰 렌더링 전에 호출됨
- Controller에서 처리한 결과를 조작하거나 추가 데이터를 뷰에 전달할때 사용됨
### afterCompletion(request, response, handler, exception)
- 뷰 렌더링을 포함한 모든 처리가 끝난 후 호출됨
- 주로 리소스 해제, 로딩 등 후처리 작업에 사용됨