- 다양한 데이터 저장소에 대한 접근을 단순화하고 표준화하는 프레임워크
- 데이터 액세스 계층(Data Access Layer)을 쉽게 구현하고
  반복적인 CRUD 작업을 줄여 개발자가 비지니스 로직에 집중할 수 있게 함
- 주요 특징
	- Repository 추상화: 데이터 액세스 계층을 위한 인터페이스 기반 추상화
	- Query method: 메서드 이름으로부터 쿼리 자동 생성
	- 페이징과 정렬: 데이터 조회 시 페이징과 정렬 기능 내장
	- Auditing: 생성 시간, 수정 시간 등의 메타데이터 관리
	- 다양한 데이터 저장소 지원: JPA, MongDB, Redis 등 지원
	- 통합 테스트 지원: Spring 프레임워크의 테스트 기능과 통합(자동롤백됨)

## Repository 추상화
- Spring Data의 가장 핵심적인 개념
- 데이터 액세스 계층을 추상화함
### Repository
- 최상위 마커 인터페이스(설명 맨아래 참조)
- 타입 정보만 제공하며 직접적인 메서드는 없음
- 도메인 클래스와 ID 타입을 타입 파라미터로 받음
### CrudRepository
- 기본적인 CRUD 기능 제공
- save, findById, findAll, delete 등 기본 작업 메서드 제공
- 간단한 데이터 접근 작업에 적합
### PagingAndSortingRepository
- CrudRepository 확장
- 페이징과 정렬 기능 추가
- 대용량 데이터 처리에 적합

## Query Method
- 메서드 이름을 분석해서 쿼리 자동 생성
	- find...By, read...By, query...By, count...By, get...By
	- 조건자: And, Or, Between, LessThan, GreaterThan 등

## 최상위 마커 인터페이스
- 메서드나 상수를 포함하지 않는 빈 인터페이스
- 어떤 속성이나 의미를 가지고 있음을 표시하는 역할을 하는 인터페이스
- `Serializable`과 `Cloneable` 인터페이스가 대표적인 마커 인터페이스임

- Spring Data 장점
	- 통합된 데이터 접근 계층 제공
	- 보일러플레이트 코드 감소
	- 다양한 데이터 저장소 지원
	- 일관된 예외처리