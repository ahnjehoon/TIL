
- run(): 새로운 스레드를 생성하지 않고 호출한 메인 스레드에서 메서드가 실행됨
- start(): 내부적으로 새로운 스레드를 생성하고 run() 메서드가 실행됨

## run()
- Thread 클래스나 Runnable 인터페이스를 구현하는 클래스에서 스레드가 해야 할 작업을 정의하는 메서드
- 단순히 해당 메서드를 호출하는 것이기 때문에, 스레드 동작이 아니라 일반 메서드 호출처럼 메인 스레드에서 실행됨
- 아래 코드는 메인스레드에서 실행되므로 멀티스레딩의 효과가 없음
```java
class SampleThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread is running in: " + Thread.currentThread().getName());
    }
}

public class Main {
    public static void main(String[] args) {
        SampleThread sampleThread = new SampleThread();
        sampleThread.run();  // 새로운 스레드를 생성하지 않음, 메인 스레드에서 실행됨
    }
}
```

## start()
- start() 메서드를 호출하면 새로운 스레드가 생성되고 그 스레드에서 run() 메서드가 실행됨
- 여기서 run() 메서드는 메인 스레드와는 독립적인 새로운 스레드에서 실행되므로 병렬 처리가 가능함
```java
class SampleThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread is running in: " + Thread.currentThread().getName());
    }
}

public class Main {
    public static void main(String[] args) {
        SampleThread sampleThread = new SampleThread();
        sampleThread.start();  // 새로운 스레드에서 run()이 실행됨
    }
}

```

