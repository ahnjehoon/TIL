- 선언적 트랜잭션 관리를 위한 어노테이션
- 프록시 기반으로 동작하며 AOP를 사용해 트랜잭션 경계를 설정함
- 적용된 Bean의 메서드 실행 전후로 트랜잭션을 자동으로 시작하고 `commit` 또는 `rollback` 함
- ACID 속성(원자성, 일관성, 고립성, 지속성)을 보장하기 위해 사용됨
	- Atomicity(원자성): 트랜잭션의 모든 작업은 전부 수행되거나 전혀 수행되지 않아야함
	- Consistency(일관성): 트랜잭션이 시작되기 전과 완료된 후의 데이터베이스 상태는 항상 일관성을 유지해야함
	- Isolation(고립성): 여러 트랜잭션이 동시에 실행될 때 서로 간섭하지 않고 독립적으로 실행되어야함
	- Durability(지속성): 트랜잭션이 완료되면 그 결과는 영구적으로 반영됨
- 
## 선언적 트랜잭션 관리
- 어노테이션 또는 XML과 같은 선언적인 방식으로 트랜잭션의 범위와 규칙을 정의하는 방식
-  코드 재사용성이 증가함
- AOP를 기반으로 트랜잭션 로직을 자동으로 적용함
- 재사용성 증가
	- 트랜잭션 처리 코드가 비지니스 로직과 분리됨
	- 단일 책임 원칙(SRP) wnstn
- 유지보수성 향상
	- 트랜잭션 정책 변경 용이
	- 중앙 집중식 트랜잭션 관리(예: 트랜잭션 타임아웃 전체 적용)

## 주요 속성
### 읽기 전용 (readOnly)
- 읽기 전용 트랜잭션 여부를 설정
- readOnly = true 시 다음과 같은 이점이 있음
	- 성능 향상
		- JPA를 사용할 경우 변경감지(dirty checking)를 안해서 성능향상
		- 엔티티가 연속성 컨텍스트에 로드될 때 스냅샷을 생성함
		- 
- 

```java
@Transactional(readOnly = true)
public void doSomething() {
    // 비지니스 로직
}
```

### 격리 수준 (isolation)
- 여러 트랜잭션이 동시에 처리될 때, 어떤 수준까지 고립시킬지 정의
- 이는 데이터의 일관성과 성능 사이의 균형을 맞추기 위해 설정함

```java
@Transactional(isolation = Isolation.READ_COMMITTED)
public void doSomething() {
    // 비지니스 로직
}
```

- READ_UNCOMMITTED: 커밋되지 않은 데이터 읽기 가능
- READ_COMMITTED: 커밋된 데이터만 읽기 가능
- REPEATABLE_READ: 동일 데이터 반복 읽기 보장
- SERIALIZABLE: 완벽한 격리, 성능 저하 가능성

### 전파 속성 (propagation)
- 현재 트랜잭션이 존재하는지 여부에 따라 새로운 트랜잭션을 생성하거나
  기존 트랜잭션에 참여하는 방식 등을 정의

``` java
@Transactional(propagation = Propagation.REQUIRED)
public void doSomething() {
    // 비지니스 로직
}
```

- REQUIRED: 기본값, 기존 트랜잭션이 있으면 참여, 없으면 새로 생성
- REQUIRES_NEW: 항상 새로운 트랜잭션 생성
- SUPPORTS: 기존 트랜잭션이 있으면 참여, 없어도 실행
- MANDATORY: 기존 트랜잭션 필수
- NEVER: 트랜잭션 없이 실행

### 롤백 설정
- 기본적으로 @Transactional은 런타임 예외(RuntimeException)가 발생했을 때 롤백함
- no/rollbackFor 속성을 통해 제어 가능

```java

@Transactional(rollbackFor = CustomException.class)
public void doSomething() {
    // 비지니스 로직
}
```

- rollbackFor: 지정된 예외 발생 시 롤백
- noRollbackFor: 지정된 예외 발생해도 롤백하지 않음

---

## 주의 사항
### 프록시 내부 호출 문제
- 프록시를 통해 동작하므로 내부 메서드 호출 시 트랜잭션 적용되지 않을 수 있음
- 같은 클래스 내에서 @Transactional이 없는 메서드가 @Transactional이 있는 메서드를 호출할 경우 트랜잭션이 적용되지 않음
- 해결방법
	- 트랜잭션 메서드를 별도 클래스로 분리
	- 자기 자신 주입(Self-Injection) 사용
### public 메서드만 적용
- private, protected 메서드에는 적용 안됨
### 예외 처리
- RuntimeException(Unchecked Exception)만 기본적으로 롤백
- Checked Exception은 롤백되지 않음
- 해결방법
	- rollbackFor 속성으로 롤백 대상 예외 지정 가능
