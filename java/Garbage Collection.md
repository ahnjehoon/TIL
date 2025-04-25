- Java의 메모리 관리 자동화 메커니즘
- JVM의 힙 메모리 영역에서 참조되지 않는 객체를 식별하고 제거하는 과정을 처리함
- 루트 집합(스택의 지역변수, 정적 변수, 활성 스레드 등)으로 부터 참조되는 대상을 Reachable이라 하고 끊기면 Unreachable로 판단

## GC 구조
### Young Generation
- Eden: 새로 생성된 객체 할당
- Survivor 0/1: Minor GC에서 살아남은 객체가 이동하는 영역. 두 영역중 하나는 비어 있어야함
- Minor GC: Eden 영역이 가득 차면 발생. Reachable 객체를 Survivor로 이동시키고 Eden을 비움
### Old Generation
- Promotion: Survivor 영역에서 일정 횟수 이상 GC를 견딘 객체가 Old 영역으로 이동
- Major GC: Old 영역이 가득 차면 발생. Stop-The-World 현상으로 어플리케이션 일시 정지

## GC 알고리즘
### 처리 방식
-  Mark-Sweep
	- Mark: 루트 집합부터 참조 추적하여 Reachable 객체 표시
	- Sweep: 표시되지 않는 Unrechable 객체 메모리 해제
- Mark-Compact: 메모리 단편화 방지를 위한 압축
- Copying: Survivor 영역 간 객체 복사로 메모리 단편화 방지
- Generational: 객체 수명에 따른 Young/Old 영역 분리
### 알고리즘
- Serial GC
	- 단일 스레드로 동작, Stop-The-World 기간 길음
	- 단일 코어/소규모 어플리케이션
- Parallel GC
	- 멀티 스레드 병렬 처리, 처리량(Throughput) 최적화
	- Java 8 기본 GC
- G1 GC
	- 힙을 Region으로 분할, 예측 가능한 중단 시간
	- Java 9 ~ 기본 GC
	- 동작 방식
		- Minor GC: Eden -> Survivor 이동 시 Garbage First 영역 우선 처리
		- Major GC: 전체 힙 최적화
- Z GC
	- 매우 빠름
	- Java 11 이상부터 지원

## 주요 특징
- 자동실행: 개발자가 병시적으로 호출 불가. JVM이 상황에 따라 주기적/비주기적 실행
- Stop-The-World: GC 실행시 모든 어플리케이션 스레드 일시 정지

