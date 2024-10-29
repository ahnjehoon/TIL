- Spring IoC 컨테이너에 의해 관리되는 객체
- Spring 설정 파일에 정의되거나 @Component, @Service, @Repository, @Controller 등의 애노테이션으로 표시됨
- 예전에는 XML에 등록했었음
- 사용하는 이유는 다음과 같음
	- 의존성 관리: 객체간 결합도를 찾추고 유연한 어플리케이션 구조를 만듦
	- 생명주기 관리: 객체의 생성, 초기화, 소멸을 일관된 방식으로 처리
	- 설정의 중앙화: 어플리케이션의 설정을 한 곳에서 관리 가능
## Scope
- Bean이 생성, 사용, 소멸 시점을 정의하는 설정
- 자원의 효율적인 관리와 적절한 객체 생명 주기 제어를 위해 사용
### Singleton(기본값)
- Spring에서 하나의 객체만 생성되어 공유됨
- 공유 자원이나 상태를 갖지 않는 stateless 서비스 클래스에 적합
### Prototype
- Bean을 요청할 때마다 새로운 객체가 생성됨
- 매번 다른 객체가 만들어지므로 상태를 가지는 stateful 서비스 클래스에 적합
### Request(Web)
- HTTP 요청마다 새로운 객체 생성
### Session(Web)
- HTTP 세션 동안 동일한 객체 유지
### Application(Web)
- Servlet Context와 동일한 생명 주기를 가짐
- 어플리케이션이 실행되는 동안 하나의 객체 유지
- 어플리케이션 전역에서 공유해야하는 설정같은 리소스 관리에 사용

## Scope Proxy
- 일반적으로 Spring Bean Scope는 Singleton임
- Singleton Bean은 어플리케이션 시작시 한번 생성되고 종료될때까지 유지되기떄문에 메모리 효율적임
- 근데 Prototype Bean처럼 매번 새로운 객체를 Singleton Bean에 주입하면 사실상 객체가 고정이되어버림
- 이런 문제를 해결하기 위해 프록시 객체가 실제 Bean 대신 주입됨
- 프록시 객체의 메서드가 호출되면 scope에 맞는 실제 Bean 객체를 가져와서 실행함

### Bean Lifecycle
- 크게 초기화, 사용, 소멸 세 단계로 나뉠 수 있음
1. Spring IoC 컨테이너 생성
	- XML, 자바 설정 클래스, 어노테이션 등을 읽어 빈 객체 등록
2. Bean 생성 및 의존성 주입
	- 등록된 Bean 정보를 기반으로 Bean 객체를 생성하고 의존관계를 주입
	- Constructor, Setter, Field 주입 방식이 사용될 수 있음
3. 초기화 콜백
	- 의존성 주입 후 초기화 작업이 필요한 경우 초기화 콜백 메서드가 호출됨
	- @PostConstruct
	- InitializingBean 인터페이스의 afterPropertiesSet() 메서드
	- @Bean의 initMethod
4. 빈 사용
5. 소멸 전 콜백
	- Bean 객체의 안전한 소멸을 위해 콜백이 호출됨
	- @PreDestroy
	- DisposableBean 인터페이스의 destroy() 메서드
	- @Bean의 destroyMethod
6. Spring 컨테이너 종료

