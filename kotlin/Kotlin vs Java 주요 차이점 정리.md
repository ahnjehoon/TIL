## 1. 변수 선언과 타입 추론

### Java

```java
String name = "John";
int age = 25;
final String constant = "VALUE";
```

### Kotlin

```kotlin
var name = "John"  // 가변 변수, 타입 추론됨
val age = 25       // 불변 변수, 타입 추론됨
val constant = "VALUE"  // 상수
```

**차이점:**

- `var`: 가변 변수 (Java의 일반 변수)
- `val`: 불변 변수 (Java의 final 변수)
- 타입 추론 자동 지원
- 명시적 타입 선언: `val name: String = "John"`

## 2. Null Safety

### Java

```java
String name = null;  // NPE 위험
if (name != null) {
    System.out.println(name.length());
}
```

### Kotlin

```kotlin
var name: String? = null  // nullable 타입
name?.let { println(it.length) }  // safe call
val length = name?.length ?: 0    // elvis operator
```

**차이점:**

- `?`: nullable 타입 명시
- `?.`: safe call operator
- `?:`: elvis operator (null일 때 기본값)
- `!!`: not-null assertion (NPE 가능)

## 3. 함수 선언

### Java

```java
public int add(int a, int b) {
    return a + b;
}

public void printMessage(String message) {
    System.out.println(message);
}
```

### Kotlin

```kotlin
fun add(a: Int, b: Int): Int {
    return a + b
}

fun add(a: Int, b: Int) = a + b  // 표현식 함수

fun printMessage(message: String) {
    println(message)
}
```

**차이점:**

- `fun` 키워드 사용
- 반환 타입은 `: Type` 형태로 뒤에 위치
- 표현식 함수 지원 (`= 표현식`)
- Unit 타입 (Java의 void) 생략 가능

## 4. 클래스와 생성자

### Java

```java
public class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

### Kotlin

```kotlin
class Person(val name: String, var age: Int)

// 또는
class Person(val name: String, var age: Int) {
    init {
        // 초기화 블록
    }
}
```

**차이점:**

- Primary constructor 지원
- 프로퍼티 자동 생성 (`val`/`var`)
- getter/setter 자동 생성
- `init` 블록으로 초기화 로직 추가

## 5. 데이터 클래스

### Java

```java
public class User {
    private String name;
    private int age;
    
    // constructor, getter, setter, equals, hashCode, toString 필요
}
```

### Kotlin

```kotlin
data class User(val name: String, val age: Int)
```

**차이점:**

- `data class`로 equals, hashCode, toString, copy 자동 생성
- 구조 분해 선언 지원: `val (name, age) = user`

## 6. 문자열 템플릿

### Java

```java
String name = "John";
int age = 25;
String message = "Hello, " + name + "! You are " + age + " years old.";
```

### Kotlin

```kotlin
val name = "John"
val age = 25
val message = "Hello, $name! You are $age years old."
val complex = "Result: ${calculate(x, y)}"
```

**차이점:**

- `$변수명` 또는 `${표현식}` 형태로 문자열 보간 지원

## 7. 확장 함수

### Java

```java
// 유틸리티 메서드 필요
public class StringUtils {
    public static boolean isEmail(String str) {
        return str.contains("@");
    }
}
```

### Kotlin

```kotlin
fun String.isEmail(): Boolean {
    return this.contains("@")
}

// 사용
val email = "test@example.com"
if (email.isEmail()) { ... }
```

**차이점:**

- 기존 클래스에 새로운 메서드 추가 가능
- 정적 메서드보다 자연스러운 문법

## 8. 스마트 캐스트

### Java

```java
if (obj instanceof String) {
    String str = (String) obj;  // 명시적 캐스팅 필요
    System.out.println(str.length());
}
```

### Kotlin

```kotlin
if (obj is String) {
    println(obj.length)  // 자동 캐스팅됨
}
```

**차이점:**

- 타입 체크 후 자동 캐스팅
- `is` 연산자 사용 (Java의 instanceof)

## 9. when 표현식

### Java

```java
switch (day) {
    case MONDAY:
    case TUESDAY:
        return "Weekday";
    case SATURDAY:
    case SUNDAY:
        return "Weekend";
    default:
        return "Unknown";
}
```

### Kotlin

```kotlin
when (day) {
    MONDAY, TUESDAY -> "Weekday"
    SATURDAY, SUNDAY -> "Weekend"
    else -> "Unknown"
}
```

**차이점:**

- `when`은 표현식 (값 반환)
- break 불필요
- 조건 범위 지원: `in 1..10`
- 타입 체크 지원: `is String`

## 10. 컬렉션과 고차 함수

### Java

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
List<String> result = names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### Kotlin

```kotlin
val names = listOf("Alice", "Bob", "Charlie")
val result = names
    .filter { it.length > 3 }
    .map { it.uppercase() }
```

**차이점:**

- 불변 컬렉션 기본 제공
- `it` 매개변수 자동 생성
- 더 간결한 람다 문법

## 11. 범위 연산자

### Java

```java
for (int i = 0; i < 10; i++) {
    System.out.println(i);
}
```

### Kotlin

```kotlin
for (i in 0..9) {
    println(i)
}

for (i in 0 until 10) {
    println(i)
}
```

**차이점:**

- `..`: 양쪽 끝 포함 범위
- `until`: 끝 값 제외 범위
- `downTo`: 역순 범위

## 12. 프로퍼티 접근

### Java

```java
person.getName();
person.setAge(30);
```

### Kotlin

```kotlin
person.name
person.age = 30
```

**차이점:**

- 프로퍼티 직접 접근 문법
- 내부적으로 getter/setter 호출됨

## 13. 기본 매개변수와 명명된 인수

### Java

```java
public void connect(String host, int port, int timeout) {
    // 오버로딩으로 기본값 구현 필요
}
```

### Kotlin

```kotlin
fun connect(host: String, port: Int = 8080, timeout: Int = 5000) {
    // 구현
}

// 사용
connect("localhost")
connect("localhost", port = 9090)
connect(host = "localhost", timeout = 10000)
```

**차이점:**

- 기본 매개변수 지원
- 명명된 인수로 가독성 향상

## 14. 접근 제어자

### Java

- `public`, `protected`, `private`, package-private (기본)

### Kotlin

- `public` (기본), `protected`, `private`, `internal`

**차이점:**

- `internal`: 같은 모듈 내에서만 접근 가능
- `public`이 기본값

## 15. 객체 선언과 싱글톤

### Java

```java
public class Singleton {
    private static final Singleton INSTANCE = new Singleton();
    private Singleton() {}
    public static Singleton getInstance() { return INSTANCE; }
}
```

### Kotlin

```kotlin
object Singleton {
    fun doSomething() { ... }
}
```

**차이점:**

- `object` 키워드로 싱글톤 간단 구현
- 지연 초기화 자동 지원