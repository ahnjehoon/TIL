- 간단히 말해서 원본 객체의 작업을 대리하여 수행하는 대리 객체임
- 원본 객체에 대한 접근을 제어하거나 추가적인 기능을 수행할 때 사용함
- 특정 객체에 대한 접근을 제한하거나 로깅, 인증, 캐싱 등의 작업을 추가할 수 있음

## Static Proxy
- 동적으로 만드는 프록시는 아님
- 컴파일 시점에 미리 생성된 프록시 클래스를 사용하는 디자인 패턴
- 캡슐화와 다형성 개념을 활용하여 만듦
- 구현이 간단하고 컴파일 타임에 오류를 발견할 수 있음
- 확장성 및 재사용성이 부족함
  인터페이스나 클래스가 많아질수록 각각의 프록시 클래스를 수동으로 생성 필요
### 예시
```java
// 인터페이스 정의
interface IService {
    void execute();
}

// 실제 작업을 수행하는 클래스
class Service implements IService {
    @Override
    public void execute() {
        try {
            Thread.sleep(1000);
            System.out.println("1초 걸리는 실제 작업 완료");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Static Proxy 클래스
class ServiceProxy implements IService {
    private Service service;

    public ServiceProxy(Service service) {
        this.service = service;
    }

    @Override
    public void execute() {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록

        System.out.println("ServiceProxy 시작");
        service.execute();
        System.out.println("ServiceProxy 종료, 걸린시간(ms) : " + (System.currentTimeMillis() - startTime));
    }
}

class StaticProxySample {
    public static void main(String[] args) {
        // 실제 작업 클래스 생성
        Service realTask = new Service();

        // Static Proxy를 사용하여 작업 실행
        IService proxy = new ServiceProxy(realTask);
        proxy.execute();
    }
}
```

```
ServiceProxy 시작
1초 걸리는 실제 작업 완료
ServiceProxy 종료, 걸린시간(ms) : 1003
```



## Dynamic Proxy
- `java.lang.reflect.Proxy` 클래스를 통해 프록시 객체를 생성하는 기술
- 런타임에 프록시 객체를 통해 메서드 호출을 가로채고 추가처리를 할 수 있음
- 프록시 설정 대상 클래스가 인터페이스 구현되어 있어야 사용 가능
- 프록시 클래스가 코드로 별도로 정의되어 있지 않아도 되어 유연성과 코드 중복을 방지할 수 있음
- 동적으로 처리되기 때문에 성능이 중요한 어플리케이션에서는 주의해야함
- AOP 및 로깅, 트랜잭션, 보안 검사등 다양한곳에서 활용됨

### 예시
```java
import java.lang.reflect.*;

interface IService {
    void execute();
}

class Service implements IService {
    public void execute() {
        System.out.println("실제 작업");
    }
}

// Logging 프록시 핸들러
class LoggingHandler implements InvocationHandler {
    private final Object target;

    public LoggingHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.currentTimeMillis();
        System.out.println("Logging 시작");
        Object result = method.invoke(target, args);
        System.out.println("Logging 종료, 걸린시간(ms) : " + (System.currentTimeMillis() - startTime));
        return result;
    }
}

// Transaction 프록시 핸들러
class TransactionHandler implements InvocationHandler {
    private final Object target;

    public TransactionHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Transaction 프록시에서 작업 전 호출: " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("Transaction 프록시에서 작업 후 호출: " + method.getName());
        return result;
    }
}

public class DynamicProxySample {
    public static void main(String[] args) {
        IService service = new Service();

        // Transaction 프록시 객체 생성 
        IService firstProxyService = (IService) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                new Class[]{IService.class},
                new TransactionHandler(service)
        );

        // Logging 프록시 객체 생성 (첫 번째 프록시를 감싸는 프록시)
        IService secondProxyService = (IService) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                new Class[]{IService.class},
                new LoggingHandler(firstProxyService)
        );

        // 두 번째 프록시를 통해 메서드 호출
        secondProxyService.execute();
    }
}
```

```text
Logging 시작
Transaction 프록시에서 작업 전 호출: execute
실제 작업
Transaction 프록시에서 작업 후 호출: execute
Logging 종료, 걸린시간(ms) : 2
```

## ByteBuddy(CGLib)
- 런타임 또는 빌드 타임에 바이트 코드를 동적으로 생성하고 조작할 수 있는 라이브러리
- 바이트 코드를 조작한다는 의미는 이미 컴파일된 클래스 파일을 기반으로 수정한다는 뜻임
- Java 9 이상에서 모듈 시스템으로 인한 원본 클래스 접근문제와 같은 호환성 이슈로
  Spring 6부터는 CGLib에서 ByteBuddy로 대체됨
- `Dynamic Proxy`는 인터페이스를 구현해야 프록시 생성이 가능했지만 ByteBuddy는 필요없음
- 바이트 코드를 직접 조작하다보니 속도도 우수함
  (`Dynamic Proxy`는 `Reflection`을 사용해서 이름을 검색하고 호출하기 때문에 느린듯)

## 성능 문제 및 최적화 방안
- proxy는 동적으로 프록시 객체를 생성하여 유연성을 제공하지만 몇가지 성능 문제가 있음
### 성능 문제
1. Reflection으로 인한 오버헤드
	- Reflection은 JVM 최적화가 제한됨
	- 메서드 인라이닝(메서드 호출을 본문 코드로 대체) 등과 같은
	  JIT 컴파일러에서 최적화 기법이 제한됨
	- 컴파일 시점에 어떤 메서드가 호출될지 알 수 없기 때문임
	- 동적 특성으로 인해 전체 코드 분석이 어려운 것도 한몫함
2. 동적 코드 생성 오버헤드
	- 런타임에 프록시 클래스 생성에 따른 추가 연산 비용
	- 많은 수의 서로 다른 인터페이스에 대한 프록시 생성 시 부담 증가
3. 메모리 사용량 증가
	- 프록시 객체 생성에 따른 추가 메모리 사용 (Metaspace)
	- 동적으로 생성된 클래스와 관련 데이터 구조로 인한 메모리 부담
4. 초기화 지연
	- 프록시 객체 생성 및 초기화에 따른 지연 발생
	- 특히 많은 수의 프록시 객체를 생성할 때 성능 저하 가능성
### 최적화 방안
1. 코드 생성 라이브러리 활용
	- CGLIB, ByteBuddy 등의 라이브러리를 사용하여 효율적인 프록시 생성
	- 바이트코드 조작을 통해 리플렉션 오버헤드 감소
	- [Byte Buddy를 사용한 런타임 코드 생성](https://blogs.oracle.com/javamagazine/post/runtime-code-generation-with-byte-buddy)
2. 캐싱 활용
	- 자주 사용되는 프록시 객체와 메서드 객체를 캐싱하여 재사용
	- 반복적인 객체 생성 및 Reflection 호출 오버헤드 감소
3. 메서드 핸들 (Java 9 이상)
	- MethodHandles와 VarHandles를 활용하여 더 효율적인 동적 메서드 호출
	- 기존 리플렉션보다 성능이 우수하며 JIT 최적화에 더 적합
```java
MethodHandles.Lookup lookup = MethodHandles.lookup();
MethodType mt = MethodType.methodType(void.class, String.class);
MethodHandle mh = lookup.findVirtual(TargetClass.class, "targetMethod", mt);

// 메서드 호출
mh.invokeExact(targetObject, "argument");
```
4. Reflection 을 최적화 하는것도 좋아보임
	[The performance implications of Java reflection](https://blogs.oracle.com/javamagazine/post/java-reflection-performance)

### 예제
```java
import java.util.concurrent.Callable;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

class Service {
    public void execute() {
        System.out.println("서비스 동작!");
    }
}

class LoggingInterceptor {
    public static void intercept(@SuperCall Callable<String> zuper) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("Logging 시작");
        zuper.call();
        System.out.println("Logging 종료, 걸린시간(ms) : " + (System.currentTimeMillis() - startTime));
    }
}

public class ByteBuddySample {
    public static void main(String[] args) throws Exception {
        Service proxy = new ByteBuddy()
                .subclass(Service.class)
                .name(Service.class.getPackage().getName() + ".ProxyClass")
                .method(ElementMatchers.named("execute"))
                .intercept(MethodDelegation.to(LoggingInterceptor.class))
                .make()
                .load(Service.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();

        proxy.execute();
    }
}
```

```
Logging 시작
서비스 동작!
Logging 종료, 걸린시간(ms) : 1
```
  
### 주의 사항
- 클래스 로더에 대해 이해가 필요함
- ByteBuddy ClassLoadingStrategy 중 두가지를 소개함
	- WRAPPER (기본값)
		- 기존 클래스를 변경하지 않고 새로운 클래스를 생성해서 JVM에 로드함
		- 원본 클래스를 두고 새로운 클래스를 주입하는 방식임
	- INJECTION
		- 기존 클래스 로더에 새로운 클래스를 주입함
		- 원래 클래스와 동일한 클래스 로더에서 새로운 클래스를 로드할 수 있음

- 이제 발생한 문제에 대해서 소개함
- 위 코드를 실행했을때 단일 파일에서 실행했었음
- `ByteBuddySample.java` 소스코드가 `ByteBuddySample.class`로 컴파일되었음
- 이 때 `Application ClassLoader`에 의해 ByteBuddySample 바이트 코드가 로드됨
- 클래스 로딩 시 전략을 별도로 지정하지 않았기에 기본값인 `WRAPPER`가 사용됨
- `WRAPPER`는 새로운 클래스 로더에서 프록시 클래스를 생성하여 로드하므로 기존 클래스와 다른 클래스 로더를 사용함
- 이는 기존 바이트코드를 대체하는 것이 아니라 새로운 클래스 로더에 프록시 클래스를 로드하는 방식임
- 자바에서는 서로 다른 클래스 로더에서 로드된 클래스는 서로를 인식하지 못함
- 따라서 원본 클래스와 프록시 클래스가 다른 클래스 로더에서 로드되어 상호작용할 수 없게 되었고 그로 인해 오류가 발생함
- 이후에 `INJECTION`으로 코드를 수정해서 기존 바이트 코드에서 수정하면 되니 오류를 뱉지 않았음

[Testing the performance of 4 Java runtime code generators: cglib, javassist, JDK proxy & Byte Buddy | jacy](https://jacyhong.wordpress.com/2015/12/01/testing-the-performance-of-4-java-runtime-code-generators-cglib-javassist-jdk-proxy-byte-buddy/)
[neoremind/dynamic-proxy: Dynamic proxy library leveraging ASM, CGLIB, ByteBuddy, Javassist and JDKDynamicProxy techniques](https://github.com/neoremind/dynamic-proxy)