- Spring Data JPA 는 JPA를 기반으로 데이터 접근 계층을 편리하게 개발할 수 있도록 도와줌
- Spring Data JPA 와 JPA 차이
	- JPA
		- `EntityManager`를 기반으로 엔티티와 트랜잭션을 관리함
	- Spring Data JPA
		- `JpaRepository` 인터페이스 기반으로 CRUD 메서드 자동 생성
		- `@Transactional` 어노테이션을 사용해서 트랜잭션 관리를 spring 내부적으로 처리하도록 위임

## EntityManagerFactory & EntityManager
- 데이터베이스와 상호작용을 담당하는 JPA의 핵심 요소
### EntityManagerFactory
- `EntityManager`를 생성하는 팩토리 객체
- 영속성 유닛(Persistence Unit)에 대한 설정 정보를 기반으로 데이터베이스 연결정보를 관리
- 어플리케이션 전체에서 하나의 인스턴스만을 생성해서 공유함
	- 커넥션 풀을 생성하는 것과 같은 리소스 사용량이 큰 작업을 수행함
	- 어플리케이션 시작시 한번만 생성하고 어플리케이션 종료시 정리함
### EntityManager
- 실제 데이터베이스와 상호작용을 하는 객체
- 엔티티(데이터베이스와 매핑된 객체)의 생명주기와 트랜잭션, 등을 관리
- 특정 트랜잭션에 종속된 엔티티를 관리하기 때문에 단일 스레드 내에서만 사용해야함

## EntityManager와 영속성 컨텍스트(Persistence Context)
- **EntityManager**는 JPA에서 **영속성 컨텍스트(Persistence Context)**를 관리하는 핵심 객체임
- **영속성 컨텍스트**는 엔티티(데이터베이스와 매핑된 객체)를 저장하고 관리하는 일종의 캐시로 데이터베이스와의 상호작용을 관리함
- 

## 사용 이유
- 생산성 향상
	- 반복적인 CRUD 작업을 위한 코드를 자동으로 생성해줌
	- Repository 인터페이스만 작성하면 구현체는 스프링이 생성해줌
- 유지보수성 개선
	- SQL을 직접 작성하지 않고 객체 중심의 개발이 가능함
	- 데이터베이스 종속성이 줄어들어 DBMS 변경 시 리스크가 감소함

## 주의사항
- 데이터 반환
	- API 응답으로 엔티티를 직접 반환하지 않고 DTO로 변환하여 사용해야함

## 동적 쿼리
- 동적쿼리 생성에는 주로 `Specifications` 또는  `QueryDSL`가 사용됨
### Specifications
- JPA Criteria API 기반임
- 

[ChatGPT](https://chatgpt.com/c/67317e79-7520-8012-9dab-31dcd3dfa662)
[Preparing for Backend Technical Interviews: Hibernate Concepts - Claude](https://claude.ai/chat/9788faee-8c77-4606-88b8-3c6fc93c2028)