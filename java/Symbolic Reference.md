- Java 코드에서 다른 클래스나 메서드, 변수를 참조할 때 코드에서는 크 클래스나 메서드의 이름만 사용함
- 아래와 같은 코드를 예시로 들어봄
```java
String hello = "world";
```
- 위에서 **String** 클래스의 이름만 적음
- 근데 **String** 이라는 이름은 실제로 어디에 있는지 모르고 이름만 참조하는 상태임
- 이게 심볼릭 레퍼런스임
- 기계는 문자만으로는 이게 어떤건지 이해를 하지 못함
- [[JVM]]의 클래스 로딩단계에서 위 문제를 해결함
- 클래스 로딩의 Resolution 단계에서 기계가 실제 메모리 주소를 참조할 수 있게 변환함