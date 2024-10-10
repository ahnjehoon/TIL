- 동시성을 해결하기 위한 방법으로는 두가지 방법이 있음
- 락 동기화(Lock-Based Synchronization)
- 락프리 동기화(Lock-Free Synchronization)

## 락 동기화(Lock-Based Synchronization)
- 상호 배제(Mutual Exclusion)를 통해 여러 스레드가 동시에 자원에 접근하는 것을 방지하는 기법

- [[Mutex(Mutual Exclusion)]]
- ReentrantLock
- ReentrantReadWriteLock
- [[Semaphore]]
- Spinlock
## 락프리 동기화(Lock-Free Synchronization)
- 락을 사용하지 않고도 스레드 간의 자원 접근을 안전하게 관리하는 기법
- 데드락(Deadlock)이나 락 경쟁(Lock Contention)을 피할 수 있음
- 스레드가 자원을 사용하기 위해 대기하지 않기 때문에 높은 성능을 제공함
- 단, 충돌이 빈번하게 발생하는 상황에서는 성능 저하가 있을 수 있음

- Compare-And-Swap (CAS)
- Atomic Variables
- Spinlock
