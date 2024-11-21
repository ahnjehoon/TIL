## Iterator

- 컬렉션 내 요소를 순차적으로 탐색하기 위해 Java 1.2 부터 추가됨

- 순차접근, 인덱스 접근 불가, 컬렉션의 구조를 변경하지 않는 한 방향 탐색

- 컬렉션의 요소를 직접적으로 제어하거나 컬렉션의 상태를 수정하는 경우에 유용함

- 주요 메서드
  
  - hasNext(), next(), remove() 등

- 사용 예시
  
  ```java
  List<String> list = Arrays.asList("a", "b", "c", "d");
  Iterator<String> iterator = list.iterator();
  while (iterator.hasNext()) {
      String element = iterator.next();
      System.out.println(element);
  }
  ```

### 순차 접근 (Sequential Access)

- 컬렉션의 요소를 첫 번째부터 시작하여 하나씩 순서대로 접근해야 한다는 의미
- 위의 예시에서 중간에 있는 c 에 접근하고 싶으면
  a → b → c 순서로만 접근할 수 있음

### 인덱스 접근 불가 (No Random Access by Index)

- Iterator 는 list 와 같이 인덱스를 통해 특정 요소에 바로 접근하는 방식이 불가능

- 예를들어 3번째 요소에 접근하고 싶으면
  리스트는 다음과 같이 접근할 수 있음
  
  ```java
  var targetElement = list.get(2);
  ```
  
  Iterator는 다음과 같이 접근해야함
  
  ```java
  var iterator =- list.iterator();
  iterator.next();
  iterator.next();
  var targetElement = iterator.next();
  ```

### 컬렉션의 구조를 변경하지 않는 한 방향 탐색

- Iterator 는 단방향으로만 이동할 수 있음
- 한번 지나간 요소로 다시 되돌아갈 수 없다는 뜻임
- 기본적으로 Iterator 는 컬렉션의 구조가 변경되지 않는 전제하에 동작함

### 설계 배경

- 위의 내용을 살펴보면 인덱스도 지원을 안하고 한번 지나가면 되돌아오지도 못하는
  기능을 굳이 왜 만들었을까 싶었음
- 다음 내용을 보면 순차적이고 단방향으로 설계된 이유를 알 수 있음
- 데이터 구조 추상화
	- 모든 컬렉션에 대해 동일한 접근 방식을 제공하기 위해 설계됨
	- List 뿐만 아니라 Set, Queue 등 다양한 컬렉션에서도 사용할 수 있음
		- 예를 들어 Set 은 원소의 순서가 정의되지 않기 때문에 인덱스기반 접근이 불가능하지만
		  일관된 탐색 방법을 제공하려면 순차적 접근이 필요함
	- 컬렉션의 내부 구조나 구현에 의존하지 않고 데이터를 탐색할 수 있도록 설계됨
	- List 는 인덱스를 제공하지만 다른 컬렉션에서는 의미가 없거나 없는 경우가 많은데,
	  Iterator 를 사용하면 모든 종류의 컬렉션에 대해 동일한 방식으로 접근할 수 있게 됨
- 자원 효율성
	- Iterator 는 현재 요소만 메모리에 유지하면서 탐색을 진행해서 대규모 컬렉션에서 유용함
	- 수백 수천만개의 데이터를 처리할 때 전체 컬렉션을 인덱스로 접근하려면 전체 메모리에 컬렉션을 올려야함. Iterator 는 하나의 요소만 가지고 작업을 계속 진행할 수 있음
- 단순성
	- hasNext(), next(), remove() 세 가지 주요 메서드만 제공하며, 이 메서드들을 통해 모든 컬렉션의 요소를 탐색할 수 있음
	- 일관되고 직관적인 탐색을 가능하게 하며 사용자가 데이터 구조의 세부 구현에 의존하지 않아도 됨
- 범용성
	- 모든 컬렉션에서 순차적 반복을 쉽게 구현할 수 있음
	- 복잡한 논리나 동작을 최소화하여 가독성과 유지보수성을 높임

## Stream

- Java 8 부터 도입된 함수형 프로그래밍 스타일

- 내부 반복(Internal iteration), 병렬 처리, 불변성 및 비상태적(State-less) 특성

- 주요 메서드
  
  - 중간연산: map, filter, sorted 등
  - 종료연산: collect, forEach, reduce 등

- 사용 예시
  
  ```java
  List<String> list = Arrays.asList("a", "b", "c");
  list.stream()
	  .filter(s -> s.startsWith("a"))
	  .forEach(System.out::println);
  ```

### 특성
#### 내부 반복 (Internal Iteration)
- 내부적으로 반복을 처리하고 병렬 처리(Parallel Processing)를 지원함
- 사용자 코드에서는 데이터 처리 방식만 정의하고 반복은 JVM이 관리
#### 지연 연산 (Lazy Evaluation)
- 중간 연산은 실행되지 않고 종료 연산이 호출될 때 연산이 수행됨
- 필요할 때만 연산을 수행하여 성능을 최적화함
#### 불변성
- 원본 컬렉션을 변경하지 않음
- 데이터를 변환하거나 필터링한 결과는 새로운 Stream 또는 컬렉션으로 반환됨

---

## 주요 차이점

|특징|Iterator|Stream|
|---|---|---|
|**반복 방식**|외부 반복 (External Iteration)|내부 반복 (Internal Iteration)|
|**지연 연산**|없음 (즉각 처리)|있음 (Lazy Evaluation)|
|**재사용성**|불가능 (일회성 사용)|불가능 (종료 후 재사용 불가, 생성은 용이)|
|**병렬 처리**|지원하지 않음 (직접 구현 필요)|병렬 처리 지원 (`parallelStream()`)|
|**성능 최적화**|단순 작업에 유리|복잡한 연산 및 병렬 처리에 유리|

---


## 결론
- Iterator 를 사용할 때
  - 단순한 작업이나 메모리 사용 최적화가 필요할 때
  - 컬렉션의 요소를 직접 수정하거나 간단한 순차 탐색이 필요한 경우
  - 예: 요소 삭제, 순차적 처리
- Stream 을 사용할 때
  - 코드 간결하게 작성할때
  - 데이터를 필터링, 변환, 집계하는 경우
  - 병렬 처리가 중요한 경우
  - 예: 대규모 데이터셋 처리, 병렬 처리
