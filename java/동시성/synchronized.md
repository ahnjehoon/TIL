- Java에서 가장 기본적인 동기화 방법
- 메서드나 블록에 적용하여 한 번에 하나의 스레드만 해당 코드에 접근할 수 있도록 만듬

> 메서드 동기화
- 한 번에 하나의 스레드만 해당 메서드를 실행할 수 있음
```java
public class Counter {
    private int count = 0;

    // 동기화된 메서드
    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}
```


> 동기화 블록
- 동기화 대상 객체를 지정할 수 있음
- 메서드 전체를 동기화하는 것보다 더 효율적일 수 있음
```java
public class Counter {
    private int count = 0;
    private final Object lock = new Object();

    public void increment() {
        synchronized (lock) {  // 이 블록만 동기화됨
            count++;
        }
    }

    public int getCount() {
        synchronized (lock) {  // 이 블록만 동기화됨
            return count;
        }
    }
}
```
