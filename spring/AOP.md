- 비지니스 로직과 별도로 여러 모듈에서 공통으로 처리하는 기능을 분리해 관리하는 방법
- 공통된 기능을 분리하여 재사용성과 유지보수성을 높임
- 로깅, 보안, 트랜잭션 관리, 예외 처리, 성능 모니터링에서 주로 사용
- 프록시 패턴을 사용하여 XML 또는 어노테이션을 통해 적용할 수 있음
- 주요 구성 요소
	- Aspect: 
	  횡단 관심사의 모듈
	  예시) 로깅, 트랜잭션 등이 Aspect 임
	- Join Point: 
	  Aspect가 적용될 수 있는 지점
	  예시) 메소드 호출 시점이나 예외 발생 시점 등이 Join Point
	- Advice: 
	  Join Point에서 수행될 실제 작업을 정의 한 것
	  예시) 메서드 호출 전후에 로그를 출력하는 작업이 Advice
	- Pointcut:
	  Aspect를 적용할 대상이나 시점을 정의하는 조건
	  이를 통해 어떤 메소드나 클래스에 Advice를 적용할지 결정함
	
	  
	  
- AOP 를 사용할때 발생할 수 있는 문제점 정리(예정)

- 대충 순수 JAVA로 부분적으로 구현해본 예제

```java
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@interface Priority {
    int value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Priority(1)
@interface Logging {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Priority(2)
@interface Transaction {
}

interface AnnotationHandler {
    void before(Object target, Method method, Object[] args);

    void after(Object target, Method method, Object[] args);
}

class LoggingHandler implements AnnotationHandler {
    private final long startTime;

    public LoggingHandler() {
        startTime = System.currentTimeMillis();
    }

    public void before(Object target, Method method, Object[] args) {
        System.out.println("Logging 시작");
    }

    public void after(Object target, Method method, Object[] args) {
        System.out.println("Logging 종료, 걸린시간(ms) : " + (System.currentTimeMillis() - startTime));
    }
}

class TransactionHandler implements AnnotationHandler {
    public void before(Object target, Method method, Object[] args) {
        System.out.println("Transaction 시작");
    }

    public void after(Object target, Method method, Object[] args) {
        System.out.println("Transaction 종료 ");
    }
}

class AnnotationProcessor implements InvocationHandler {
    private final Object target;
    private final Map<Class<? extends Annotation>, AnnotationHandler> handlers;

    public AnnotationProcessor(Object target) {
        this.target = target;
        this.handlers = findAnnotationHandlers();
    }

    private Map<Class<? extends Annotation>, AnnotationHandler> findAnnotationHandlers() {
        Map<Class<? extends Annotation>, AnnotationHandler> handlersMap = new HashMap<>();

        handlersMap.put(Logging.class, new LoggingHandler());
        handlersMap.put(Transaction.class, new TransactionHandler());

        return handlersMap;
    }

    private List<AnnotationHandler> getSortedHandlers(Method method) {
        List<AnnotationHandler> sortedHandlers = new ArrayList<>();

        for (Annotation annotation : method.getAnnotations()) {
            AnnotationHandler handler = handlers.get(annotation.annotationType());
            if (handler != null) {
                sortedHandlers.add(handler);
            }
        }

        sortedHandlers.sort(Comparator.comparingInt(h -> getPriority(h.getClass())));

        return sortedHandlers;
    }

    private int getPriority(Class<?> handlerClass) {
        if (handlerClass.isAnnotationPresent(Priority.class)) {
            return handlerClass.getAnnotation(Priority.class).value();
        }
        return Integer.MAX_VALUE;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());

        // Priority에 따른 핸들러 정렬
        List<AnnotationHandler> handlers = getSortedHandlers(targetMethod);

        // before 호출
        for (AnnotationHandler handler : handlers) {
            handler.before(target, targetMethod, args);
        }

        // 실제 메서드 실행
        Object result = targetMethod.invoke(target, args);

        // Priority 역순위로 after() 호출
        for (int i = handlers.size() - 1; i >= 0; i--) {
            handlers.get(i).after(target, targetMethod, args);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new AnnotationProcessor(target));
    }
}

interface IService {
    void execute();
}

class Service implements IService {
    @Logging
    @Transaction
    public void execute() {
        System.out.println("실제 작업 실행");
    }
}

public class AnnotationSample {

    public static void main(String[] args) throws Exception {
        IService service = AnnotationProcessor.createProxy(new Service());
        service.execute();
    }
}

```

