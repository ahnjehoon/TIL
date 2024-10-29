## Servlet
- Java를 이용해 Web Server에서 동작하는 프로그램
- 사용자의 요청을 처리하고 응답을 생성함

## Servlet Container
- Servlet을 관리하고 실행함
- 사용자의 요청을 받아서 Servlet에게 전달하고
  Servlet이 요청을 처리한 결과를 사용자에게 반환함
  
  
- Tomcat 같은 Application Server가 Servlet Container임

- Java를 사용하여 웹페이지를 동적으로 생성하는 서버 사이드 프로그램
- 사용자의 요청을 처리하고 응답을 생성하는 Java 클래스

## lifecycle
- Servlet Container는 사용자의 요청마다 Servlet 객체를 생성하지 않고 재사용함
### 1. Servlet 초기화(최초 로딩시)
- Servlet Container가 Servlet에 대한 요청을 받으면 Servlet 객체를 생성하고 초기화함
  (또는 loading-on-startup 설정을 통해 초기화 될 수 있음)
- 최초 요청을 받으면 init() 메서드가 호출되어 Servlet이 초기화됨
- 초기화된 Servlet 객체는 하나의 객체로 관리되며 재사용됨
### 2. 요청 처리(doGet/doPost 메서드 호출)
- 사용자의 요청을 받으면 Servlet Container는 기존에 생성된 Servlet 객체를 재사용함
- Servlet의 service() 메서드를 호출하고 
  요청 방식에 따라 doGet() 또는 doPost() 같은 메서드가 호출되어 요청을 처리함


!중요 위에부분 Spring 사용할때는 Servlet 안타고 DispatcherServlet 타니깐 재정리 필요함

- Servlet Container는 각 요청마다 HttpServletRequest와 HttpServletResponse 객체를 생성함
	- HttpServletRequest
	  사용자가 보낸 요청과 관련된 모든 정보를 담고 있음(Http 메서드, 요청 파라미터, 헤더 등)
	- HttpServletResponse
	  Servlet 처리 이후 사용자에게 돌려줄 응답 정보를 담을 때 사용됨
- Thread pool 에서 Thread 할당
- 



## 동시성 문제는 없는가?

## currentRequestAttributes vs getRequestAttributes
- RequestContextHolder에는 똑같이 RequestAttributes 를 반환하는 메서드 `getRequestAttributes()` `currentRequestAttributes()` 가 존재함
- 두 메서드의 차이는 다음과 같음
    - getRequestAttributes()
        - 스레드에 직접적으로 바인딩 된 RequestAttribute를 가져옴
        - 먼저 thread local에서 RequestAttribute를 찾고 없을 경우 상속 가능한 thread local 저장소를 탐색함
    - currentRequestAttributes()
        - 처음에는 getRequestAttribute() 와 똑같이 로직을 탐
        - 근데도 없다? 그럼 JSF가 존재할때 FacesContext를 통해 RequestAttrivute를 가져옴

[java - what is the difference between these methods of RequestContextHolder , currentRequestAttributes() and getRequestAttributes()? - Stack Overflow](https://stackoverflow.com/questions/47586707/what-is-the-difference-between-these-methods-of-requestcontextholder-currentre)

[spring-framework/spring-web/src/main/java/org/springframework/web/context/request/RequestContextHolder.java at main · spring-projects/spring-framework (github.com)](https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/web/context/request/RequestContextHolder.java#L99-L140)