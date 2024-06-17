# XA

- 하나의 어플리케이션에서 다중 DB를 연결할 때가 있음
- 이때 하나의 business logic 에서 다중 DB에 접근하여 데이터를 제어할 때
  한군데서 오류가 발생하면 데이터 일관성을 위해 Transaction Rollback 기능이 필요함
  (ex. 은행의 송금, 재고 정보 등..)
- 각 서비스마다 커밋한 뒤에도 Rollback 처리를 해도 되지만
  이를 해결하기 위한 방법이 있음

## XA 개요

- 분산 환경에서 여러 DB의 트랜잭션을 관리하기 위한 표준화된 프로토콜과 API
  (DB 뿐만이 아닌 메시징 시스템 과 같은 환경도 포함되는듯)
- X/Open Association 에서 정의한 분산 트랜잭션 처리를 위한 표준

---

## 특징

- 일관성
  
  분산 환경에서 데이터 일관성 유지 가능

- 독립성
  
  각 Resource Manager의 독립적인 처리를 보장하면서 전체 트랜잭션의 원자성을 유지함

- 효율성
  
  여러 리소스 간의 병렬 처리가 가능하여 성능을 최적화 할 수 있음

---

## 구성요소

### Transaction Manager

- XA 트랜잭션의 중심적인 역학을 수행하는 컴포넌트
- 트랜잭션의 시작, 커밋, 롤백 등을 관리
- 분산 환경에서 다수의 리소스 관리자와 통신하여 트랜잭션의 일관성과 독립성 보장

### Resource Manager

- Transaction Manager와 상호 작용하여 자신이 관리하는 리소스를 제어
- XA 트랜잭션 프로토콜에 따라 트랜잭션의
  참여, 준비(prepare), 커밋(commit), 롤백(rollback) 등의 단계를 수행함

---

## XA 트랜잭션 동작 단계

1. 트랜잭션 시작
   
   Transaction Manager 는 여러 Resource Manager 에게 트랜잭션을 시작하라는 신호를 보냄

2. 트랜잭션 실행
   
   각 Resource Manager 는 트랜잭션에 참여하고 해당 리소스에서 작업을 수행

3. 트랜잭션 준비
   
   모든 Resource Manager 가 작업을 완료하면 Transaction Manager 에게 준비 완료를 알림

4. 트랜잭션 커밋 또는 롤백
   
   - 커밋 요청 시, Transaction Manager 는 모든 Resource Manager 에게 커밋을 요청함
   - 롤백 요청 시, Transaction Manager 는 모든 Resource Manager 에게 롤백을 요청함

---

## 주의 사항

- 오버헤드
  
  - 분산 환경에서 트랜잭션 처리는 추가적인 오버헤드를 발생시킬 수 있음

- Resource Manager 지원 유무 확인
  
  - DB 또는 시스템이 XA 트랜잭션을 지원하지 않을 수 있음
    
    - Oracle 은 9i 또는 10g
    
    - MySQL 은 5
    
    - PostgreSQL 은 8.2
    
    - SQL server (MsSQL) 은 2012 부터인듯
      
      근데 SQL Server 는 Microsoft Distributed Transaction Coordinator(DTC) 를 사용하고
      이를 기반으로 XA 트랜잭션 기능을 지원하는 듯
