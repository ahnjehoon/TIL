## Lambda Expression
- Java 8 에서 도입된 함수형 프로그래밍 기법(상태 변화와 가변 데이터를 멀리하는 선언형 프로그래밍 방식. 동일 입력 -> 동일 출력, 함수를 값처럼 전달/반환 등)
- 기존 익명 클래스 방식보다 코드를 훨씬 간단하고 읽기 쉽게 만들어줌

### 장점
- 코드 간결성: 불필요한 코드를 제거하여 가독성 향상
- 함수형 프로그래밍: 함수를 값처럼 취급 가능
- 병렬 처리: Stream API와 함께 사용하여 효율적인 병렬 처리 가능
- 지연 연산: 필요할 때만 연산을 수행

### 예시
```java
// 기존 방식
Comparator<Integer> comp1 = new Comparator<Integer>() {
    public int compare(Integer a, Integer b) {
        return a.compareTo(b);
    }
};

// 람다식
Comparator<Integer> comp2 = (a, b) -> a.compareTo(b);


// 한 줄인 경우 return 키워드 생략
Function<Integer, Integer> square = x -> x * x;

// 여러 줄인 경우 return 키워드 필요
Function<Integer, String> converter = x -> {
    if (x > 0) return "양수";
    else if (x < 0) return "음수";
    else return "영";
};

// Method Reference
// 람다식
Function<String, Integer> f1 = s -> Integer.parseInt(s);

// 메서드 참조
Function<String, Integer> f2 = Integer::parseInt;

```

### Stream API 와 차이점
- Lambda: "표현 방법"
- Stream API: "처리 도구"

|항목|Lambda|Stream API|
|---|---|---|
|정의|함수형 인터페이스를 간결하게 구현하는 문법|컬렉션 데이터를 함수형 방식으로 처리하는 API|
|역할|동작(Behavior)을 전달하기 위한 표현|데이터를 필터링, 매핑, 집계 등 처리|
|형태|`(a, b) -> a + b`|`list.stream().filter(x -> x > 10)`|
|관계|Stream 내부 연산에서 자주 사용됨|Lambda를 내부적으로 활용함|
|독립성|단독으로도 사용 가능|내부적으로 Lambda를 거의 필수로 사용|
