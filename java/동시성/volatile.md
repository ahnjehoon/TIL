- Java에서 변수의 가시성(visibility)을 보장하는 키워드
- Mutli thread 환경에서 변수의 최신 값을 항상 읽을 수 있도록 보장
- 위의 방식이 가능한 이유는 보통 CPU의 캐시메모리의 값을 주 메모리에 반영함
- 근데 volatile은 RAM같은 메모리에 다이렉트로 접근해서 데이터를 변경하기 때문임

> 변수 선언
- volatile 키워드를 변수 선언 시 사용
- 해당 변수에 대한 모든 읽기와 쓰기가 메인 메모리에서 직접 이루어짐

```java
public class SharedFlag {
    private volatile boolean flag = false;

    public void setFlag(boolean value) {
        flag = value;
    }

    public boolean isFlag() {
        return flag;
    }
}
```

> 사용 사례
- 상태 플래그로 사용할 때 유용
- 한 스레드가 쓰고 다른 스레드들이 읽는 경우에 적합

```java
public class Worker implements Runnable {
    private volatile boolean running = true;

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            // 작업할거
        }
    }
}
```

## 주의사항
- volatile은 원자성을 보장하지 않음
- 복합 연산(예: i++)에는 적합하지 않음
- 1.데이터를 읽고 -> 2.값을 계산하고 -> 3.값을 변경함
- 위 3가지 작업이 있기 때문임
