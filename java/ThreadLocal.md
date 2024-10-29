- 각 스레드가 독립적으로 값을 가질 수 있도록 하는 클래스
- 스레드마다 고유한 값을 사용하므로 동기화 없이도 안전하게 사용 가능
- 주로 Session, Transaction Context와 같은 스레드에 국한된 데이터를 저장할 때 사용

```java
public class ThreadLocalSample {
    private static ThreadLocal<Integer> threadLocalValue = ThreadLocal.withInitial(() -> 1);

    public static void main(String[] args) {
        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName() + " init value: " + threadLocalValue.get());
            try {
                while (true) {
                    threadLocalValue.set(threadLocalValue.get() + 1);
                    System.out.println(Thread.currentThread().getName() + " value: " + threadLocalValue.get());
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + " interrupted");
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();
    }
}
```

## 사용시 주의해야할점
- 위의 예제는 ThreadLocal 값을 제거하는 로직이 필요 없어서 `remove()`를 작성하지 않았음
### 메모리 문제
- 요청 처리 후에는 반드시 `remove()` 메서드를 호출해서 메모리를 해제해야 함
- 그렇지 않을 경우 다음과 같은 문제가 발생함
- 스레드 풀을 사용하는 같은 환경에서 이전에 값이 남아 있을 수 있어서 오작동을 일으킬 수 있음
- ThreadLocal이 참조하고 있는 객체가 오랫동안 GC 대상이 되지 않을 수 있어서 메모리 누수로 이어짐
### 복잡성 증가
- 각 스레드에서 독립적으로 값을 유지해서 디버깅이나 유지보수가 어려워짐
- 코드 복잡성을 증가시킬 수 있음


