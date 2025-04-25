- Java에는 C/C++ 과 같은 언어에서 사용하는 포인터가 존재하지 않음
	- 여기서 포인터란 데이터가 저장된 메모리의 주소값을 저장하는 변수임
	- 메모리의 특정위치를 가리키는 변수라고 보면됨
- JAVA는 메모리 안전성과 보안을 위해 직접적인 메모리 조작을 허용하지 않음
- 대신 참조(Reference) 개념을 사용해서 간접적으로 접근함
- 메모리 주소를 직접 다루지 않고도 객체에 접근할 수 있도록 설계되어 있기 때문임
- Java의 참조 변수는 객체를 가리킴
- 변수면 값을 가르키는거 아님? 이라고 생각할수도 있음
- 위 내용은 맞기도 하고 틀리기도함
- 이 내용을 확인하려면 Java의 데이터 타입을 알아야함
- 그리고 더 나아가서 메모리 구조까지 알아야함

---

## 데이터 타입
### 원시 타입(Primitive Type)
- 정수형: byte, short, int, long
- 실수형: float, double, char, boolean
- 문자형: char
- 논리형: boolean
### 박스형 타입(Boxed Type, Wrapper Class)
- Java 5부터 지원되는 오토박싱(Auto-Boxing) 기능으로 자동 변환됨
- null 값을 가질수 있고 원시타입보다 메모리 사용량이 많음
- 정수형: Byte, Short, Integer, Long
- 실수형: Float, Double
- 문자형: Character
- 논리형: Boolean
### 참조형 타입(Reference Type)
- Array
- Class(String 등)
- Interface
- Enum

---

## 메모리 영역
### 스택(Stack) 메모리
- 원시 타입의 값 저장
- 참조형 타입의 주소값 저장
- 메서드 호출 정보 저장
### 힙(Heap) 메모리
- 객체의 실제 데이터 저장
- 가비지 컬렉션 대상 영역
- 동적으로 크기가 변할 수 있음

---

## 값 저장 방식

- Java는 값을 저장하기 위해 스택(Stack) 메모리 영역과 힙(Heap) 메모리 영역을 사용함
- 원시 타입일 경우에는 스택 메모리 영역만 사용함
- 원시타입은 스택에 값 자체를 저장하기 때문에 null 을 할당할 수 없음
  원시/참조형타입을 구분하려고 이렇게 설계됨
- 박스형,참조형 타입일 경우에는 스택과 힙 메모리영역 둘다 사용함(아래에서 추가설명)
- 박스형,참조형 타입은 스택에 힙을 가리키는 메모리 주소값을 저장하고
  힙에 실제 값을 저장함
- 추가로 박스형,참조형 타입은 스택에 null 을 저장할 수 있음
- 그러므로 스택과 힙 메모리영역 둘 다 사용된다는 위 글은 잘못됨
- 박스형타입은 스택과 힙 메모리 영역을 사용하거나 스택만 사용한다 라고 해야함
- null 일 경우에는 힙 메모리 영역을 사용하지 않기 때문임

---

## 예시
### NPE가 발생하는 대표적인 상황
``` java
// 1. 객체 참조 없이 메서드 호출
String str = null;
int length = str.length();

// 2. 배열이 null인 상태에서 접근
int[] array = null;
int first = array[0];

// 3. 컬렉션의 null 요소 접근
List<String> list = null;
list.add("item");
```

### 값 전달과 참조 전달
```java
public class Example {
    public static void main(String[] args) {
        // 원시 타입 전달 (값 복사)
        int number = 10;
        System.out.println("Before: " + number);
        notChanged(number);
        System.out.println("After: " + number);
        
        System.out.println("==============");
        
        // 참조 타입 전달 (주소 복사)
        int[] numberArray = {10};
		System.out.println("Before: " + numberArray[0]);
		isChanged(numberArray);
		System.out.println("After: " + numberArray[0]);
    }

    public static void notChanged(int x) {
        x = 100;
    }

    public static void isChanged(int[] x) {
        x[0] = 200;
    }
}
```
```
Before: 10
After: 10
==============
Before: 10
After: 200
```