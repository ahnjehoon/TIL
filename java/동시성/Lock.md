- **synchronized** 키워드보다 더 세밀한 동기화 제어를 제공
- 대표적으로 **ReentrantLock** 클래스가 있음

> ReentrantLock
- 명시적으로 락을 획득하고 해제할 수 있기 때문에, 더 복잡한 락 제어가 가능함
- lock.lock()으로 락을 획득하고
  무조건 lock.unlock()을 호출하여 락을 해제해야 함
- try-finally 구문을 사용하여 락이 반드시 해제되도록 보장
```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private int count = 0;
    private Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();  // 락을 획득
        try {
            count++;
        } finally {
            lock.unlock();  // 락을 해제
        }
    }

    public int getCount() {
        lock.lock();  // 락을 획득
        try {
            return count;
        } finally {
            lock.unlock();  // 락을 해제
        }
    }
}
```

> ReentrantReadWriteLock
- 자원이 자주 수정되지만, 동시에 여러 스레드가 자원을 읽어야 하는 경우사용

> StampedLock
- Java 8 에서 도입되었으며 낙관적 읽기 락(Optimistic Read Lock)을 사용함
- 자원의 수정보다 읽는 경우가 많을 때 ReentrantReadWriteLock 보다 성능이 좋음

> Condition
- Lock과 함께 사용되어 스레드 간의 통신을 가능하게 함
- 주요 메서드
	- await()
	  스레드를 대기 상태로 만듬
	  다른 스레드가 signal()이나 signalAll()을 호출할 때까지 대기함
	- signal(): 하나의 스레드를 깨움
	- signalAll(): 대기 중인 모든 스레드를 깨움