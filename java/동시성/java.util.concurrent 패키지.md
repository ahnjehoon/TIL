- 동기화와 스레드 안전성을 보장하는 도구들이 모여있음

>Atomic
- 기본 타입(Primitive Type)에 대한 원자적(atomic) 연산을 제공
```java
import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();  // 원자적 증가 연산
    }

    public int getCount() {
        return count.get();
    }
}
```

>Concurrent Collection
- ConcurrentList, ConcurrentHashMap 같은 Thread Safe 한 컬렉션을 제공함
```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class SharedMap {
    private Map<String, Integer> map = new ConcurrentHashMap<>();

    public void putValue(String key, int value) {
        map.put(key, value);
    }

    public int getValue(String key) {
        return map.get(key);
    }
}
```

