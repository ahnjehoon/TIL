- 런타임에 클래스, 메서드, 필드 등에 접근하여 동적으로 조작할 수 있는 기술
- 다음과 같은 기능 제공
  1. 클래스, 메서드, 필드 정보 탐색
  2. 동적 객체 생성
  3. 동적 메서드 호출
  4. 접근 제한자를 무시하고 필드에 접근
- 주로 프레임워크(Spring, Hibernate), 테스트(Junit, Mockito), 디버깅용으로 reflection이 사용됨
- 컴파일 타임이 아닌 런타임중에 코드를 동적으로 조작하는 것은 장단점이 존재함
- 일단 JVM이 런타임중에 메타데이터를 분석하는 과정때문에 일반 코드보다 느림
- 런타임에 클래스를 다루기 때문에 컴파일 타임에 에러를 검출하지 못하므로 안전성이 떨어짐
- private나 protected 접근제어자를 무시하고 사용할 수 있기 때문에 보안문제도 있음
- IDE를 사용할 때 코드 추적이 어려워서 유지보수가 떨어지기도 함
- 잘 써야함

## 주요 클래스

- Reflection API 관련 클래스들은
  Java의 기본 패키지(`java.lang`, `java.lang.reflect`)에 포함되어 있어서
  별도로 import 하지 않아도됨

- Reflection을 사용하려면 먼저 `Class` 객체를 가져와야함

- `Class` 객체는 클래스 자체의 메타데이터를 담고있음

- `Class` 객체는 JVM의 `ClassLoader`가 클래스를 메모리에 로드할 때
  클래스의 구조를 분석하여 메타데이터를 `Class` 객체에 저장함
  
  ### Class - java.lang.Class

- 클래스의 메타데이터(이름, 부모 클래스, 구현된 인터페이스, 필드, 메서드)에 접근할 수 있는 클래스

- Reflection API의 중심이 되는 클래스
  
  ### Method - java.lang.reflect.Method

- 클래스에 선언된 메서드 정보에 접근할 수 있는 클래스

- 메서드의 이름, 반환타입, 매개변수 타입 등 메서드와 관련된 정보를 제공하고 동적 호출 가능
  
  ### Field - java.lang.reflect.Field

- 클래스에 선언된 멤버 변수(Field) 정보에 접근할 수 있는 클래스

- 필드 값을 동적으로 읽고 쓸 수 있음
  
  ### Constructor - java.lang.reflect.Constructor

- 클래스에 선언된 생성자 정보에 접근할 수 있는 클래스

- 생성자를 동적으로 호출하여 객체를 생성할 수 있음
  
  ### Proxy - java.lang.reflect.Proxy

- 런타임에 동적으로 프록시 객체를 생성할 수 있도록 도와주는 클래스

- 주로 AOP(Aspect Oriented Programming)나 인터페이스를 통한 동적 기능 추가에 사용됨

## Class( java.lang.Class)

- Java에서 클래스의 메타데이터에 접근할 수 있는 가장 기본적인 클래스

- Java Reflection API의 핵심

- 모든 클래스는 JVM에서 로드되면 해당 클래스에 대한 `Class` 객체가 생성되고
  이를 통해 클래스의 구조와 관련된 정보를 동적으로 접근가능함
  
  ### 주요 메서드

- getClass(): 객체의 `Class` 객체 반환
  
  ```java
  Class<?> clazz = instance.getClass();
  ```

- getName(): 클래스 이름 반환
  
  ```java
  String className = MyClass.class.getName();
  ```

- getDeclaredMethods(): 클래스에 선언된 모든 메서드(private, protected, public) 반환
  
  ```java
  Method[] methods = MyClass.class.getDeclaredMethods();
  ```

- getMethods(): 클래스와 상위 클래스에서 상속된 public 메서드만 반환
  
  ```java
  Method[] methods = MyClass.class.getMethods();
  ```

- getDeclaredFields(): 클래스에 선언된 모든 필드를 반환
  
  ```java
  Field[] fields = MyClass.class.getDeclaredFields();
  ```

- getDeclaredConstructors(): 클래스에 선언된 모든 생성자를 반환
  
  ```java
  Constructor<?>[] constructors = MyClass.class.getDeclaredConstructors();
  ```

- getSuperclass(): 상위 클래스 반환
  
  ```java
  Class<?> superclass = MyClass.class.getSuperclass();
  ```

## Method(java.lang.reflect.Method)

- Method 클래스는 클래스에 정의된 메서드에 대한 정보를 제공하고
  메서드를 런타임에 호출할 수 있는 기능을 제공함

- 메서드의 이름, 반환 타입, 매개변수 타입 등을 동적으로 얻을 수 있고 메서드 호출도 가능
  
  ### 주요 메서드

- getName(): 메서드의 이름을 반환
  
  ```java
  String methodName = method.getName();
  ```

- getReturnType(): 메서드의 반환 타입을 반환
  
  ```java
  Class<?> returnType = method.getReturnType();
  ```

- getParameterTypes(): 메서드의 매개변수 타입 배열을 반환
  
  ```java
  Class<?>[] parameterTypes = method.getParameterTypes();
  ```

- getModifiers(): 메서드의 접근 제한자와 관련된 modifier(public, private 등)를 반환
  
  ```java
  int modifiers = method.getModifiers();
  ```

- invoke(Object obj, Object... args): 해당 메서드를 동적으로 호출
  
  ```java
  method.invoke(instance, args);
  ```

## Field(java.lang.reflect.Field)

- 클래스에 선언된 멤버 변수(Field) 정보 제공

- 변수의 값을 동적으로 읽거나 설정할 수 있음

- private 필드 또한 변경 가능
  
  ### 주요 메서드

- getName(): 필드 이름 반환
  
  ```java
  String fieldName = field.getName();
  ```

- getType(): 필드 타입 반환
  
  ```java
  Class<?> fieldType = field.getType();
  ```

- get(Object obj): 해당 객체의 필드 값 반환
  
  ```java
  Object value = field.get(instance);
  ```

- set(Object obj, Object value): 해당 객체의 필드 값 설정
  
  ```java
  field.set(instance, newValue);
  ```

- setAccessible(boolean flag): private 필드에 접근할 수 있도록 설정
  
  ```java
  field.setAccessible(true);
  ```

## Constructor(java.lang.reflect.Constructor)

- 클래스 생성자에 대한 정보를 제공

- 생성자를 통해 객체를 동적으로 생성할 수 있음

- private 생성자 또한 변경 가능
  
  ### 주요 메서드

- getName(): 생성자 이름 반환
  
  ```java
  String constructorName = constructor.getName();
  ```

- getParameterTypes(): 생성자 매개변수 타입 배열 반환
  
  ```java
  Class<?>[] paramTypes = constructor.getParameterTypes();
  ```

- newInstance(Object... initargs): 해당 생성자를 사용해 객체를 동적으로 생성
  
  ```java
  Object instance = constructor.newInstance(arg1, arg2);
  ```

- setAccessible(boolean flag): private 생성자에도 접근할 수 있도록 설정
  
  ```java
  constructor.setAccessible(true);
  ```

## Proxy(java.lang.reflect.Proxy)

- 런타임에 동적으로 프록시 객체를 생성할 수 있도록 도와주는 클래스

- 프록시는 대리 객체로 원래 객체 대신 요청을 처리하는 역할을 함

- `Proxy` 객체는 원래 객체와 동일한 인터페이스를 구현해야 함

- Dynamic Proxy
  
  - Java에서 동적으로 생성되는 프록시 객체
  - 인터페이스를 기반으로 프록시 객체를 생성함

- InvocationHandler
  
  - `Proxy` 객체는 모든 메서드 호출을
    `InvocationHandler` 인터페이스의 `invoke()` 메서드로 전달함
  - `Proxy` 객체가 호출되면
    `InvocationHandler`는 메서드 이름과 인수 등의 정보 바탕으로 동작을 제어함

- AOP(Aspect Oriented Programming), 트랜잭션, 프록시 패턴 구현에 사용됨
  
  ### 주요 메서드

- `newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)`
  
  - 동적으로 `Proxy` 객체 생성
  - `ClassLoader`는 동적 프록시 클래스의 클래스 로더를 지정
  - `interfaces`는 프록시가 구현해야하는 인터페이스 배열
  - `InvocationHandler`는 `Proxy` 객체가 호출될 때 실행할 로직을 정의하는 핸들러

- `isProxyClass(Class<?> cl):` 주어진 클래스가 프록시 클래스인지 확인
  
  ```java
  boolean isProxy = Proxy.isProxyClass(proxyObject.getClass());
  ```

- getInvocationHandler(Object proxy): 프록시 객체에 연결된 `InvocationHandler` 반환
  
  ```java
  InvocationHandler handler = Proxy.getInvocationHandler(proxyObject);
  ```
  
### 예시

```java
import java.lang.reflect.*;

interface IService {
    void task();
}

class Service implements IService {
    public void task() {
        System.out.println("실제 작업");
    }
}

// 첫 번째 프록시 핸들러
class FirstInvocationHandler implements InvocationHandler {
    private final Object nextProxy;

    public FirstInvocationHandler(Object nextProxy) {
        this.nextProxy = nextProxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("첫 번째 프록시에서 작업 전 호출: " + method.getName());
        Object result = method.invoke(nextProxy, args);
        System.out.println("첫 번째 프록시에서 작업 후 호출: " + method.getName());
        return result;
    }
}

// 두 번째 프록시 핸들러
class SecondInvocationHandler implements InvocationHandler {
    private final Object realService;

    public SecondInvocationHandler(Object realService) {
        this.realService = realService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("두 번째 프록시에서 작업 전 호출: " + method.getName());
        Object result = method.invoke(realService, args);
        System.out.println("두 번째 프록시에서 작업 후 호출: " + method.getName());
        return result;
    }
}

public class ProxySample {
    public static void main(String[] args) {
        IService service = new Service();

        // 첫 번째 프록시 생성 
        IService firstProxyService = (IService) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                new Class[]{IService.class},
                new FirstInvocationHandler(service)
        );

        // 두 번째 프록시 생성 (첫 번째 프록시를 감싸는 프록시)
        IService secondProxyService = (IService) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                new Class[]{IService.class},
                new SecondInvocationHandler(firstProxyService)
        );


        // 두 번째 프록시를 통해 메서드 호출
        secondProxyService.task();
    }
}
```

```text
두 번째 프록시에서 작업 전 호출: task
첫 번째 프록시에서 작업 전 호출: task
실제 작업
첫 번째 프록시에서 작업 후 호출: task
두 번째 프록시에서 작업 후 호출: task
```