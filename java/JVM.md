
![[JVM.svg]]

- 대략적인 Java 어플리케이션의 실행과정은 다음과 같음
	1. 소스 코드 작성
	2. 컴파일
	3. 클래스 로딩
	4. 실행
	5. GC 및 메모리 관리
- 1은 개발자가 .java 확장자를 가진 소스파일 만드는 단계
- 2는 소스 코드를 바이트 코드로 변환하여 .class 파일로 만드는 단계

## 클래스 로딩(Class Loading)
- Java 어플리케이션이 실행될때 JVM의 Class Loader가 클래스 파일을 메모리에 로드함
- 클래스 로딩 과정은 다음과 같음
	1. 로딩(Loading)
	   클래스 파일을 읽어들여 JVM의 ==Method Area==에 로드함
	2. 연결(Linking)
		- 검증(Verification)
		  클래스 파일이 JVM 명세에 명시된 대로 구성되어 있는지 검사함
		- 준비(Preparing)
		  클래스 또는 인터페이스의 모든 클래스 변수(static variable)가 메모리에 할당됨
		  그리고 각 변수 타입에 따라 기본값으로 초기화됨
		  즉, 실제 값은 할당되지 않고 메모리 공간만 차지하는 상태
		- 해결(Resolution)
		  [[Symbolic Reference]]가 실제 메모리 주소로 변환됨
	3. 초기화(Initialization)
	   클래스 변수들을 코드에서 정의한 값으로 할당함
- 클래스 로더는 세 가지 유형으로 구분됨
	- Bootstrap ClassLoader
	  JVM 핵심 라이브러리를 로드함
	  가장 기본적인 클래스 로더
	- Extension ClassLoader
	  확장 클래스를 로드함
	  JAVA_HOME/lib/ext 에서 클래스를 로드
	- Application ClassLoader
	  사용자 정의 클래스 및 패키지를 로드함
- 클래스 로더 로딩 전략
	- 클래스 로더는 기본적으로 Delegation Model 을 사용하여 클래스를 로딩함
	- 이 과정은 최상위 클래스 로더인 Bootstrap ClassLoader 부터 시작해서 하위 로더로 전달됨
	- 순서는 다음과 같음
	  이 순서는 Application ClassLoader 에서 시작됐다고 가정함
		1. Application ClassLoader
		   Extension ClassLoader에게 클래스를 로드할 수 있는지 요청함
		2. Extension ClassLoader
		   Bootstrap ClassLoader에게 클래스를 로드할 수 있는지 요청함
		3. Bootstrap ClassLoader
		4. 역순으로 복귀
		   Bootstrap ClassLoader가 클래스를 찾지 못한 경우 로딩 요청이 다시 Extension ClassLoader로 돌아옴
		   Bootstrap ClassLoader > Extension ClassLoader > Application ClassLoader 순으로 찾음
		   근데 Application ClassLoader에서도 클래스를 못찾으면 **ClassNotFoundException** 이 발생함
	   - 위와 같은 Delegation Model을 사용하는 이유는 클래스 로딩의 안전성, 일관성, 보안성을 확보하기 위함이라고 함

## 실행(Execution)
- 클래스가 로드되고 초기화되면 JVM은 바이트코드를 실행함
- JVM이 바이크코드를 해석하는 두가지 주요 방식이 있음
	1. 인터프리터(Interpreter)
	   바이트코드를 한줄씩 해석하여 실행하는 방식
	   시작은 빠르지만 매번 해석해야해서 실행속도는 느림
	2. JIT(Just In Time)
	   인터프리터 방식의 성능 한계를 보완하기 위해 사용됨
	   반복적으로 실행되는 바이트코드를 기계어로 변환해 캐싱함

## Runtime Data Area
- Method Area
  클래스 구조 정보(클래스, 메서드, 인터페이스 등)를 저장
- Heap
  동적으로 생성된 객체가 저장되는 영역
  Garbage Collection의 관리 대상 영역
- 개별 스레드마다 생성되는 영역
	- Stack
	  메서드 호출에 따른 **스택프레임**(지역 변수, 매개변수, 리턴 값 등)을 관리함
	  스택프레임은 메서드가 호출될 때마다 생성되고 종료되면 제거됨
	- PC Register
	  현재 스레드가 어떤 부분을 무슨 명령으로 실행해야 할지 판단하는 **JVM 명령어 주소를 관리**
	- Native Method Stack
	  Java 이외의 네이티브 코드(C, C++ 등)로 작성된 메서드가 실행될 때 사용

## Garbage Collector
- Heap 메모리를 정리하여 가용 메모리를 확보해줌
- GC Root는 Heap 메모리 영역을 참조하는 요소임
  (Method Area의 클래스 변수, Stack의 로컬 변수, Native Method Stack의 JNI 참조 등이 있음)
- GC Root로부터 그래프 순회를 통해 **Heap과 연결된 객체를 찾아 식별(Mark)** 하고
  참조되지 않은(Unreachable) 객체는 Heap에서 제거(Sweep) 함
- 위 과정을 **Mark And Sweep** 이라고 함
- 그래서 Method Area, Stack, Native Method Stack 영역
  추가로 Native Memory의 Metaspace 영역과 간접 상호작용함


# Native Memory
- 위에서 설명하지 않은 Native Memory 영역이 있음
- Native Memory 영역은 JVM이 OS에서 직접 할당받아 사용하는 메모리임
## 주요 구성 요소
1. Code Cache
   JIT 에서 반복적으로 사용되는 코드를 기계어로 변환하여 저장하는 영역
2. Metaspace
   Java8 부터 PermGen 영역을 보완하기 위해 추가됨
   클래스 메타데이터(필드, 메서드, 생성자 등) 저장
   Method Area 의 실질적인 정보가 이 영역에 저장됨
3. Thread Stack
   Runtime Data Area의 Stack 정보가 실질적으로 저장되는 영역
4. C Heap
   JVM 내부에서 사용되는 다양한 데이터나 네이티브 객체의 정보가 실제로 저장되는 영역
5. Etc

# JVM Thread 호출시 메모리 구조
```text
+-----------------------------------------+
|               Method Area               |
|  +-----------------------------------+  |
|  | Class Metadata                    |  |
|  | Runtime Constant Pool             |  |
|  | Static Variables                  |  |
|  +-----------------------------------+  |
+-----------------------------------------+
|                 Heap                    |
|  +-----------------------------------+  |
|  | Young Generation                  |  |
|  |  + Eden                           |  |
|  |  + Survivor (S0, S1)              |  |
|  +-----------------------------------+  |
|  | Old Generation                    |  |
|  +-----------------------------------+  |
+-----------------------------------------+
|             Main Thread Stack           | <--- 메인 스레드 호출 스택
|  +-----------------------------------+  |
|  | Frame                             |  |
|  |  + Local Variables                |  |
|  |  + Operand Stack                  |  |
|  |  + Return Address                 |  |
|  +-----------------------------------+  |
+-----------------------------------------+
|          New Thread Stack (if any)      | <--- 새로운 스레드 호출 스택
|  +-----------------------------------+  |
|  | Frame                             |  |
|  |  + Local Variables                |  |
|  |  + Operand Stack                  |  |
|  |  + Return Address                 |  |
|  +-----------------------------------+  |
+-----------------------------------------+
|         Main Thread PC Register         | <--- 메인 스레드 명령 주소 저장
+-----------------------------------------+
|         New Thread PC Register          | <--- 새로운 스레드 명령 주소 저장
+-----------------------------------------+
|         Native Method Stack(s)          | <--- 네이티브 메서드 호출 시 사용
+-----------------------------------------+
```
