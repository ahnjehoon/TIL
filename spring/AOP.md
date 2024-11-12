- 비지니스 로직과 별도로 여러 모듈에서 공통으로 처리하는 기능을 분리해 관리하는 방법
- 공통된 기능을 분리하여 재사용성과 유지보수성을 높임
- 로깅, 보안, 트랜잭션 관리, 예외 처리, 성능 모니터링에서 주로 사용
- 프록시 패턴을 사용하여 XML 또는 어노테이션을 통해 적용할 수 있음
- 주요 구성 요소
	- Aspect
		- 횡단 관심사를 모듈화하는 단위
		- 시스템 전반에 걸쳐 영향을 미치는 관심사를 캡슐화함
		- 예시) 로깅, 트랜잭션 등
	- Join Point
		- Aspect가 적용될 수 있는 지점
		- 메서드 실행, 생성자 호출, 필드 값 변경 등이 해당됨
	- Pointcut
		- 특정 Advice가 적용될 Join Point 정의
	- Advice
		- 실제로 Join Point에서 실행될 코드
		- 종류
			- @Before: 메서드 실행 전
			- @After: 메서드 실행 후
			- @AfterReturning: 정상 반환 후
			- @AfterThrowing: 예외 발생 시
			- @Around: 메서드 실행 전후
	- 말로만 해서는 어려움 예시 보는게 좋은듯

## 주의 사항
### Spring AOP는 메서드 레벨에서만 동작함
- 필드 접근이나 클래스 초기화 같은 세밀한 제어가 필요한 경우 AspectJ를 사용해야함
### 자가 호출 문제
- 같은 클래스 내의 메서드끼리 호출할 떄 AOP가 적용되지 않음
	- 내부 메서드를 호출할때는 proxy를 거치지 않기 때문에 발생하는 문제임
	- 특히 `@Transaction`을 사용시 트랜잭션이 누락되어 문제가 됨
- 해결방법
	- 자기 자신을 의존성 주입(self-injection)받아서 실행
	- 관련 메서드를 별도 클래스로 분리
### 성능 오버헤드 증가
- 특히 Spring AOP 같은 경우 proxy 객체를 생성해서 메서드 호출 시점에 AOP를 적용함
- 런타임에 바이트코드를 조작하는 방식은 시스템 자원을 추가적으로 소모함
### 유지보수 비용 증가
- Pointcut을 잘못 정의하면 원하지 않는 메서드에 Advice가 적용될 수 있음
- 또한 순환 참조 문제나 AOP 가 적용된 메서드의 동작으로 인한 테스트의 어려움도 유지보수 비용을 증가시키는 원인이 될 수 있음

## JoinPoint vs ProceedJoinPoint
- `ProceedJoinPoint`는 메서드 실행 전/후로 제어하고 싶을 떄 사용함(`@Around`에서만 사용)
### JoinPoint
- `@Before`, `@After`, `@AfterReturning`, `@AfterThrowing`에서 사용
- 메서드 실행에 대한 정보만 제공
- 실행 제어 불가(proceed() 메소드 없음)
### ProceedJoinPoint
- `@Around`에서만 사용
- proceed() 메서드를 통해 메서드 실행을 제어할 수 있음
- 메서드 실행 전/후에 로직 추가 가능

## AOP 예시 - 1
- 위에서 주요 구성요소에 대한 설명이 이해가 잘안가는데 코드보면 대략 이해가 될것임
- Join Point
	- 위의 코드에서 OrderService의 모든 메서드 실행 지점이 JoinPoint가 됨
	- createOrder
	- getOrder
	- cancelOrder
- Pointcut
	- `@Pointcut("execution(* com.example.service.OrderService.*(..))")`
	  OrderService 모든 메서드를 대상으로 지정
	- `@Pointcut("@annotation(com.example.annotation.RequireStock)")`
	  `@RequireStock` 어노테이션이 붙은 메서드만 대상으로 지정
- Advice
	- `@Around`, `@Before`, `@After`, `@AfterReturning`, `@AfterThrowing` 같은 애들
- Aspect
	- OrderAspect 클래스가 하나의 Aspect
	- 주문 처리와 관련된 관심사(로깅, 성능 측정, 재고 확인)를 모듈화

```java
@Data
public class OrderDto {
    private Long orderId;
    private String productId;
    private int quantity;
    private String customerInfo;
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireStock {
}

@Service
public class OrderService {
    @RequireStock
    public void createOrder(OrderDto orderDto) {
        // 주문 생성 로직
    }

    public OrderDto getOrder(Long orderId) {
        // 주문 조회 로직
        return new OrderDto();
    }

    public void cancelOrder(Long orderId) {
        // 주문 취소 로직
    }
}

@Aspect
@Component
@Slf4j
public class OrderAspect {
    
    // Pointcut 정의
    @Pointcut("execution(* com.example.service.OrderService.*(..))")
    private void orderServiceMethods() {}
    
    @Pointcut("@annotation(com.example.annotation.RequireStock)")
    private void stockCheckMethods() {}
    
    // Before Advice: 메소드 실행 전 파라미터 로깅
    @Before("orderServiceMethods()")
    public void logParameters(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        log.info("Executing {} with parameters: {}", methodName, Arrays.toString(args));
    }
    
    // Around Advice: 메소드 실행 시간 측정
    @Around("orderServiceMethods()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            String methodName = joinPoint.getSignature().getName();
            log.info("Method {} executed in {} ms", methodName, (endTime - startTime));
        }
    }
    
    // After Returning Advice: 메소드 반환값 로깅
    @AfterReturning(pointcut = "orderServiceMethods()", returning = "result")
    public void logResult(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {} returned: {}", methodName, result);
    }
    
    // After Throwing Advice: 예외 처리 및 로깅
    @AfterThrowing(pointcut = "orderServiceMethods()", throwing = "ex")
    public void handleException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error("Exception in {}: {}", methodName, ex.getMessage());
    }
    
    // Around Advice: 재고 확인
    @Around("stockCheckMethods()")
    public Object checkStock(ProceedingJoinPoint joinPoint) throws Throwable {
        // 재고 확인 로직
        OrderDto orderDto = extractOrderDto(joinPoint.getArgs());
        if (orderDto != null && !hasEnoughStock(orderDto)) {
            throw new InsufficientStockException("재고가 부족합니다.");
        }
        return joinPoint.proceed();
    }
    
    private OrderDto extractOrderDto(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> arg instanceof OrderDto)
                .map(arg -> (OrderDto) arg)
                .findFirst()
                .orElse(null);
    }
    
    private boolean hasEnoughStock(OrderDto orderDto) {
        // 재고 확인 로직 구현
        return true;
    }
}

```



## AOP 예시 - 2
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

- 위의 코드를 AOP를 사용한 코드로 수정하면 다음처럼 됨
```java
@Aspect
@Component
@Order(1)
public class LoggingAspect {
    @Around("@annotation(Logging)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        System.out.println("Logging 시작");

        Object proceed = joinPoint.proceed();

        System.out.println("Logging 종료, 걸린시간(ms): " + (System.currentTimeMillis() - startTime));
        return proceed;
    }
}

@Aspect
@Component
@Order(2)
public class TransactionAspect {
    @Around("@annotation(Transaction)")
    public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Transaction 시작");

        Object proceed = joinPoint.proceed();

        System.out.println("Transaction 종료");
        return proceed;
    }
}

public interface IService {
    void execute();
}

@Service
public class Service implements IService {
    @Logging
    @Transaction
    public void execute() {
        System.out.println("실제 작업 실행");
    }
}

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);

        IService service = context.getBean(IService.class);
        service.execute();
    }
}

```