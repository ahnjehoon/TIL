- JAVA 코드에 메타데이터(속성, 구조, 특성 등 정보)를 추가하는 방법
- `@`가 붙은 식별자는 컴파일러에 의해 어노테이션으로 해석됨
- 메서드, 클래스, 필드, 생성자 등 다양한 곳에 적용될 수 있음

## Metadata
- 데이터에 대한 정보
- 특정 데이터의 속성, 구조, 특성 등에 대한 정보를 제공하여 데이터를 관리하는 것을 의미
- 예시
	- 책
	  제목, 저자, 출판일, 장르, ISBN 번호 등이 메타데이터
	- 이미지 파일
	  크기, 파일 형식(JPEG, PNG 등), 생성일, 해상도 등이 메타데이터
- JAVA에서 어노테이션은 메타데이터 표현방식중 하나임

## Annotation 작성
- 어노테이션은 인터페이스와 유사하게 `@interface` 키워드를 사용하여 정의할 수 있음
  (특별한 형태의 인터페이스임)
- Reflection과 어노테이션을 결합하면 동적인 프로그래밍이 가능함

### 메타 어노테이션
- 어노테이션을 정의할 때 사용되는 특수 어노테이션
- 주요 메타 어노테이션은 다음과 같음
- `@Retention`
	- 어노테이션의 유지 정책을 정의함
	- `RetentionPolicy.RUNTIME`: 런타임까지 유지되어서 Reflection을 통해 접근 가능
	- `RetentionPolicy.CLASS`: 
	  `.class` 파일에 컴파일되어 저장되지만 JVM에서 로딩되지 않음
	- `RetentionPolicy.SOURCE`: 컴파일 과정에서 어노테이션을 무시함
- `@Target`
	-  어노테이션이 적용될수 있는 위치를 지정
	- `ElementType.METHOD`: 메서드에 적용 가능
	- `ElementType.FIELD`: 필드에 적용 가능
	- `ElementType.TYPE`: 클래스나 인터페이스에 적용 가능
- `@Inherited`
	- 자식 클래스에 상속되도록 설정
- `@Documented`
	- Javadoc에 포함되도록 설정


```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@interface Priority {
    int value() default 1;
}
```

### Repeatable & Container Annotation
- Java8 부터 적용됨
- 동일한 타입의 어노테이션을 여러개 사용할 수 있게 해주는 기능
- `@Repeatable` 어노테이션으로 동일한 어노테이션을 여러번 적용할 수 있음
- Container Annotation은 배열 형태의 value() 메서드를 반드시 포함해야함

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Schedules.class)
@interface Schedule {
    String day();
    String time();
}

public class SampleScheduler {
    @Schedule(day = "Monday", time = "10:00")
    @Schedule(day = "Tuesday", time = "14:00")
    public void doSomething() {
	    System.out.println("하이");
    }
}
```


```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Schedule {
    String day();
    String time();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Schedules {
    Schedule[] value();
}


public class SampleScheduler {
	@Schedules({
	    @Schedule(day = "Monday", time = "10:00"),
	    @Schedule(day = "Tuesday", time = "14:00")
	})
    public void doSomething() {
	    System.out.println("하이");
    }
}

```

