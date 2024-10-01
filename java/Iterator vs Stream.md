- 미완성본

- 궁금한 사항에 대해 자료 탐색하고 보충 설명 끼워 넣는중

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
- 데이터 추상화
  - 모든 컬렉션에 대해 동일한 접근 방식을 제공하기 위해 설계됨
  - List 뿐만 아니라 Set, Queue 등 다양한 컬렉션에서도 사용할 수 있음
    예를 들어 Set 은 원소의 순서가 정의되지 않기 때문에 인덱스기반 접근이 불가능하지만
    일관된 탐색 방법을 제공하려면 순차적 접근이 필요함
  - 컬렉션의 내부 구조나 구현에 의존하지 않고 데이터를 탐색할 수 있도록 설계됨
    List 는 인덱스를 제공하지만 다른 컬렉션에서는 의미가 없거나 없는 경우가 많은데,
    Iterator 를 사용하면 모든 종류의 컬렉션에 대해 동일한 방식으로 접근할 수 있게 됨
- 자원 효율성
  - Iterator 는 현재 요소만 메모리에 유지하면서 탐색을 진행해서 대규모 컬렉션에서 유용함
  - 수백 수천만개의 데이터를 처리할 때 전체 컬렉션을 인덱스로 접근하려면 전체 메모리에 컬렉션을 올려야함. Iterator 는 하나의 요소만 가지고 작업을 계속 진행할 수 있음
- 단순성 및 안전성
  - 작성중

## Stream

- Java 8 부터 도입된 함수형 프로그래밍 스타일

- 내부 반복(Internal iteration), 병렬 처리, 불변성 및 비상태적(State-less) 특성

- 주요 메서드
  
  - 중간연산: map, filter, sorted
  - 종료연산: collect, forEach, reduce

- 사용 예시
  
  ```java
  List<String> list = Arrays.asList("a", "b", "c");
  list.stream().forEach(System.out::println);
  ```

## 주요 차이점

### 외부 반복 (External iteration) vs 내부 반복 (Internal iterarion)

- Iterator
  - 외부 반복 을 사용함
  - 외부 반복이란? ?????
- Stream
  - 내부 반복 을 사용함
  - 내부 반복이란??????

### 지연 연산 (Lazy Evaluation)

- Iterator
  - 즉각적으로 요소를 순회함
  - 요청된 요소를 즉시 반환
  - ????? 그럼 바로바로 계산한다는 의미인지
    그래서 컬렉션에 바로 적용이 된다는 소리인지?
- Stream
  - 지연 연산을 사용함
  - 중간 연산이 호출될 때 실제로 데이터를 처리하지 않고
    종료 연산이 호출될 때 최종적으로 데이터를 처리함

### 재사용성

- Iterator
  - 일회성 사용
  - 순회가 끝나면 동일한 Iterator 사용 불가
- Stream
  - Stream 이 닫히면 다시 사용할 수 없음
  - 하지만 Stream 을 생성하는 것은 간편하고 함수형 스타일로 연산을 쉽게 반복할 수 있음
  - ???? 예시내ㅂ나

### 병렬 처리

- Iterator
  - 순차적으로 하나씩 요소를 처리함
  - 멀티 스레딩을 직접 구현하지 않는 한 병렬처리에 적합하지 않음
- Stream
  - parallelStream() 메서드를 통해 병렬 처리를 쉽게 적용 할 수 있음
  - 단, 주의점 내놔!!!!!???ㅏㅁ너이ㅏㅓ

### 성능

- Iterator
  - 순차적으로 작업을 처리하므로 전체적인 연산이 간단하고 메모리 소모가 적음
  - 복잡한 데이터 변환이나 필터링은 불리할 수 있음
- Stream
  - 연산을 최적화하며 ???? 병렬 처리를 통해 성능향상이 가능함
  - 오버 헤드가 발생할 수 있음 ??? 왜 ?????
  - 대용량 데이터를 다룰 때 Stream 의 이점이 큼 왜???????????????

## 결론

- Iterator 는 요소 제거가 가능하지만, Stream 은 불변성을 유지하며 원본 컬렉션을 수정하지 않음
  컬렉션 자체의 변경이 필요한 경우라면 Iterator 가 적합할 수 있음

- 복잡한 데이터를 처리할때(필터링, 집계, 변환 등) 는 Stream 을 사용할 때
  코드가 간결해지고 가독성이 좋아짐

- Iterator 를 사용할 때
  
  - 컬렉션의 구조를 직접 수정
  - 순차적인 탐색이 필요한 경우

- Stream 을 사용할 때
  
  - 필터링, 변환, 집계 등 다단계 데이터 처리
  - 병렬 처리가 성능에 중요한 경우
