- 데이터를 효율적으로 저장하고 관리하기 위한 자료구조
- List, Set, Map, Queue 등 핵심 인터페이스를 기반으로 함

## 계층 구조
- Collection: 추가, 삭제, 검색과 같은 기본 작업 정의
	- List: 순서 있음. 요소 중복 O 인덱스 접근 가능
	- Set: 순서 없음. 요소 중복 X
	- Queue: 주로 FIFO 방식으로 동작
- Map: Key-Value 쌍의 매핑. 키 중복X. 값 중복O

```
Collection
├── List
│   ├── ArrayList
│   ├── LinkedList
│   └── Vector
├── Set
│   ├── HashSet
│   ├── LinkedHashSet
│   └── TreeSet
└── Queue
    ├── LinkedList
    └── PriorityQueue

Map
├── HashMap
├── LinkedHashMap
├── TreeMap
└── Hashtable

```

## 주요 컬렉션
### ArrayList
- 인덱스를 통한 빠른 접근 O(1)
- 동적 크기 조정(내부 배열 재할당)
- 배열 기반으로 메모리 지역성이 좋아 캐시 효율성 좋음
### LinkedList
- List와 Queue 인터페이스를 구현
- 중간에 요소 삽입/삭제가 효율적(O(1), 위치를 알고 있는 경우)
- 요소 접근은 O(n)으로 느림
- 각 노드가 이전 노드와 다음 노드 참조(이중 연결 리스트)
### Vector
- 모든 메서드가 동기화된 Thread-safe
- ArrayList와 비슷하지만 동기화로 인한 성능 오버헤드
- 레거시 클래스로 잘 안씀
### HashSet
- 해시 테이블을 사용하여 요소 저장
- 빠른 추가, 제거, 검색(평균O(1)))
- 요소 순서 보장 안함
- 내부적으로 HashMap 사용
- hashCode()와 equals() 메서드를 사용하여 중복 판단
### LinkedHashSet
- HashSet 확장
- 이중 연결 리스트를 추가하여 요소의 삽입 순서 유지
- HashSet보다 약간의 추가 메모리 사용
### TreeSet
- 이진 검색 트리(Red-Black Tree) 기반으로 요소가 정렬되어 있음
- 추가, 제거, 검색 모두 O(logN)으로 HashSet보다 느림
- 요소는 Comparable 또는 Comparator 제공 필요
### HashMap
- 키에 대한 빠른 접근, 삽입, 삭제(평균 O(1))
- 키와 값으로 null 허용
- 내부적으로 해시 테이블(버킷 배열)을 사용
- Java8부터 해시충돌시 LinkedList에서 Red-Black Tree 로 전환

## 컬렉션 순회
### Iterator
- 컬렉션을 순회하기 위한 표준 방법을 제공함
- hasNext(): 다음 요소 존재 여부 확인
- next(): 다음 요소 반환
- remove(): 마지막으로 반환된 요소 제거
### ListIterator
- List 컬렉션을 순회하기 위한 양방향 반복자
- hasPrevious(): 이전 요소 존재 여부 확인
- previous(): 이전 요소 반환
- add(param): 현재 위치에 요소 추가
- set(param): 마지막으로 반환된 요소 대체
### for-each
- 내부적으로 iterator 사용
- 컬렉션 수정시 ConcurrentModificationException이 발생할 수 있음
### Stream API
- 선언적 프로그래밍: 무엇을 할지 정의할고 어떻게 할지는 추상화
- 함수형 프로그래밍 스타일: 불변성과 부작용 없는 함수 지향
- 지연평가: 필요할 때만 연산 수행
- 병렬 처리: parrallelStream()으로 멀티 코어 활용 간소화
- 파이프라이닝: 여러 연산을 체이닝하여 가독성 향상
- 중간 결과 없이 연산 체이닝: 임시 컬렉션 생성 감소
- 중간 연산: 지연 평가 되어 종단 연산이 호출될 때까지 실행되지 않음(filter, map, sorted 등)
- 종단 연산: 스트림 파이프라인의 결과를 산출하고 스트림을 소비(collect, count, anyMatch 등)

## 면접질문

### 삽입/삭제가 빈번하게 일어나는 경우 ArrayList vs LinkedList
- 일반적으로 LinkedList가 적합함
	- 각 요소가 포인터로 연결된 노드의 집합임
	- 중간에 요소를 삽입하거나 삭제해도 다른 요소를 이동시킬 필요가 없음
- ArrayList는 배열 기반이므로 요소 이동 비용이 큼
	- 내부적으로 배열을 사용함
	- 중간에 요소를 삽입하거나 삭제하면 모든 요소들을 이동시켜야함
- 만약 리스트의 뒤쪽에서 빈번하면 ArrayList가 유리하긴함