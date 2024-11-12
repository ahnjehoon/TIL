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
- 주의사항
	- IoC 컨테이너가 소멸을 관리하지 않기 때문에 `@PreDestroy` 가 호출되지 않음
### Request(Web)
- HTTP 요청마다 새로운 객체 생성
### Session(Web)
- HTTP 세션 동안 동일한 객체 유지
### Application(Web)
- Servlet Context와 동일한 생명 주기를 가짐
- 어플리케이션이 실행되는 동안 하나의 객체 유지
- 어플리케이션 전역에서 공유해야하는 설정같은 리소스 관리에 사용

## Proxy Mode
- 일반적으로 Spring Bean Scope는 Singleton임
- Singleton Bean은 어플리케이션 시작시 한번 생성되고 종료될때까지 유지되기떄문에 메모리 효율적임
- 근데 Prototype Bean처럼 매번 새로운 객체를 Singleton Bean에 주입하면 사실상 객체가 고정이되어버림
- 이런 문제를 해결하기 위해 프록시 객체가 실제 Bean 대신 주입됨
- 프록시 객체의 메서드가 호출되면 scope에 맞는 실제 Bean 객체를 가져와서 실행함
- AOP에서 많이 사용되며 트랜잭션, 로깅, 보안 등의 기능을 적용할 때 메서드 호출을 가로채서 전/후에 추가 로직을 삽입하는 방식으로 사용됨
- 주요 활용 사례
	- 트랜잭션 관리
	- `Request` 및 `Session` Scope 빈 관리
	- 로깅 및 보안
- 주의사항
	- 성능저하
		- 프록시를 통해 메서드에 접근하면 가로채기 과정이 발생해서 성능이 저하됨
	- 타입 캐스팅
		- `ScopeProxyMode.INTERFACE` 모드에서는 인터페이스 기반으로 프록시가 생성되어 구체 클래스에 대한 타입 캐스팅이 불가능함
		- 이를 해결하려면 인터페이스 주입이나 `TARGET_CLASS`를 사용해야함
	- `this` 참조
		- 프록시 객체는 실제 빈을 감싸고 있지만 메서드 내부에 `this` 키워드로 다른 메서드 호출시 프록시가 적용되지 않음
		- 프록시가 적용된 메서드 호출을 외부에서 호출할 수 있도록 해야함

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

## @Bean 어노테이션
- 메서드 수준에서 Bean을 정의하기위해 사용
- 해당 메서드가 반환하는 객체를 Spring IoC 컨테이너가 관리하는 Bean으로 등록함
### 주요 속성
- name: Bean의 이름 지정
- initMethod: 초기화 메서드 지정
- destroyMethod: 소멸 메서드 지정

## @Bean과 @Component 차이
- `@Bean`
	- 메서드에 선언하여 반환되는 객체를 Bean으로 등록하는 방식
	- 사용
		- 외부 라이브러리 클래스의 Bean 설정 필요시
		- 초기화 및 소멸 메서드 설정이 필요할때
		- 객체 생성 과정에서 복잡한 초기화나 주입이 필요한 경우
- `@Component`
	- 클래스에 선언하여 해당 클래스를 Bean으로 등록하는 방식
	- 사용
		- 일반적인 Service, Repository 같은 어플리케이션 로직 클래스에 사용
		- 자동 스캔을 통해 Bean 등록시
		- 간단한 Bean 등록시

## @Bean과 @Configuration 차이
- `@Configuration`은 여러 개의 `@Bean` 메서드를 포함하는 설정 클래스
- `@Configuration` 클래스는 CGLIB 프록시로 처리됨
- 내부 `@Bean` 메서드가 여러 번 호출되어도 싱글톤 Bean으로 관리됨
- `@Component`는 일반적인 클래스로 프록시 처리되지 않고
  각 메서드가 호출될 때마다 새로운 객체가 반환될 수 있음
- `@Bean` 간의 호출 순서를 제어하려면 `@DependsOn("{Bean 이름}")`을 사용하면됨

## Bean 순환참조
- Spring Boot 2.6 이상에서는 순환 참조가 기본적으로 금지되어 있음
- 필드 주입이나 `@Lazy` 어노테이션을 활용해 순환 참조 문제를 피할 수 있음
- 단, `@Lazy`를 사용할 경우 초기화 시점이 늦춰지므로 Bean이 언제 초기화 될지 모름



## 초기화 및 종료에 관여하는 방법
### InitializingBean과 DisposableBean 인터페이스
- `InitializingBean` 인터페이스는 `afterPropertiesSet()` 메서드를 구현하여 빈의 초기화 로직을 정의할 수 있음
- `DisposableBean` 인터페이스는 `destroy()` 메서드를 구현하여 빈이 소멸될 때 호출될 로직을 정의할 수 있음
```java
public class SampleBean implements InitializingBean, DisposableBean {
    @Override
    public void afterPropertiesSet() {
        // 초기화 로직
    }
    
    @Override
    public void destroy() {
        // 종료 로직
    }
}
```
### @Bean 어노테이션의 initMethod와 destroyMethod 속성
- `@Bean`을 사용할 때 `initMethod`와 `destroyMethod` 속성을 지정하면
  초기화와 종료 시 실행할 메서드를 설정할 수 있음
- `@PostConstruct`와 `@PreDestroy` 없이도 초기화 및 종료 로직을 구현할 수 있음
```java
@Configuration
public class AppConfig {
    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    public SampleBean sampleBean() {
        return new SampleBean();
    }
}
```
### 


## 주의 사항
### @PostConstruct 
- `@PostConstruct`는 Bean이 생성되고 의존성이 주입된 후 호출됨
	- 이 시점에 다른 빈이나 의존성이 준비되지 않은 경우가 있을 수 있는지 확인해야함
- 가급적 간단한 초기화 작업만 수행해야 함
	- 어플리케이션의 시작속도가 느려지거나 예기치 못한 오류가 발생할 수 있음
	- 별도의 초기화 메서드로 분리하거나 비동기적으로 처리하는것이 좋음
### @PreDestory 리소스
- `@PreDestory`는 Bean이 소멸되기전에 호출됨
- 특정 상황에서 `@PreDestory`가 호출되지 않을 수 있음
  (강제 종료, JVM 비정상 종료, @Scope("prototype))
- 모든 리소스를 정리하려고 의존하는 방식은 피해야 함
- 해결방법
	- 가능한 `try-with-resources` 같은 블록에서 직접관리
### 공유 자원 관리
- 전역 변수나 공유 자원을 설정하는 경우 다른 Bean이나 스레드에서 동기화 문제가 발생할 수 있음
- 해결 방법
	- 전역 상태 대신 각 Bean이 독립적으로 상태를 관리해야함
	- 필요한 경우 동기화 메커니즘을 고려해야함
### @PostConstruct @PreDestory 예외 처리
- `@PostConstruct`와 `@PreDestory`에서 발생한 예외는 
  다른 Bean 생성이나 종료에 영향을 줄 수 있음
- 해결 방법
	- 예외 처리