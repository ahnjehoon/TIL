- Java에는 C/C++ 과 같은 언어에서 사용하는 포인터가 존재하지 않음
- 여기서 포인터란 데이터가 저장된 메모리의 주소값을 저장하는 변수임
  간단하게 메모리의 특정위치를 가리키는 변수라고 보면됨
- JAVA는 메모리 안전성과 보안을 위해 직접적인 메모리 조작을 허용하지 않음
- 대신 참조(Reference) 개념을 사용해서 간접적으로 접근함
- 메모리 주소를 직접 다루지 않고도 객체에 접근할 수 있도록 설계되어 있기 때문임

- 개발을 하다보면 NPE(NullPointerException) 문제를 경험해본적이 있을것임
- 근데 위에서는 포인터를 직접적으로 사용하지 않는다고 해서 이상하게 느껴질것임


## 원시 타입(Primitive Type)과 참조형 타입(Reference Type)
- Java의 참조 변수는 객체를 가리킴
- 변수면 값을 가르키는거 아님? 이라고 생각할수도 있음
- 위 내용은 맞기도 하고 틀리기도함
- 이 내용을 확인하려면 Java 의 원시 타입과 참조형 타입을 알아야함
- 그리고 더 나아가서 메모리 구조까지 알아야함
- 원시 타입(Primitive Type)은 다음과 같음
  byte, short, int, long, float, double, char, boolean
- 참조형 타입은 Array, Class, Enum, Interface 가 있음
- 참조형 타입들은 객체를 사용하여 실제 값을 저장함
- 이중 대표적인 박스형 타입을 예시로 들어봄
- 박스형 타입(Boxed Type, Wrapper Class)은 다음과 같음
  Byte, Short, Integer, Long, Float, Double, Character, Boolean
- 객체를 생성할때 new 키워드를 사용해서 생성하는데 얘네들은 그런적이 없을거임
- Java 5 부터 지원한 오토 박싱(Auto-Boxing) 때문에 그럼
- 박스형 타입은
- Java는 값을 저장하기 위해 스택(Stack) 메모리 영역과 힙(Heap) 메모리 영역을 사용함
- 원시 타입일 경우에는 스택 메모리 영역만 사용함
- 원시타입은 스택에 값 자체를 저장하기 때문에 null 을 할당할 수 없음
  원시/참조형타입을 구분하려고 이렇게 설계됨
- 참조형 타입일 경우에는 스택과 힙 메모리영역 둘다 사용함
- 참조형 타입은 스택에 힙을 가리키는 메모리 주소값을 저장하고
  힙에 실제 값을 저장함
- 추가로 참조형 타입은 스택에 null 을 저장할 수 있음
- 그러므로 스택과 힙 메모리영역 둘 다 사용된다는 위 글은 잘못됨
- 박스형타입은 스택과 힙 메모리 영역을 사용하거나 스택만 사용한다 라고 해야함
- null 일 경우에는 힙 메모리 영역을 사용하지 않기 때문임

### 

- 매개변수의 값이 어떨때는 바뀌고 어떨때는 안바뀌는 경험이 이런것때문에 그럼
- 예시를 들어봄
```java
public class Example {
    public static void main(String[] args) {
        int number = 10;
        System.out.println("Before: " + number);
        notChanged(number);
        System.out.println("After: " + number);
        
        System.out.println("==============");
        
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
