# Nested Transactions 중첩 트랜잭션

## 용어 정리

- database(이하 `DB`)
- stored procedure(이하 `SP`)
- transaction(이하 `TX`)

## 학습 배경

- 3개(MES, WMS, ERP)의 각기 다른 DB가 있었음
  2개는 동일한 시스템에서 관리되고 1개는 다른 시스템에서 관리됨
- 근데 3개를 묶어서 사용하는 로직이 존재함
  `XA Transaction` 을 사용하면 되지만
  ERP는 타사에서 관리되는 시스템이여서 `XA Transaction` 사용이 가능한지
  확실하지가 않은 상황임
- 임시 해결방법은 다음과 같았음
  1. MES, WMS `XA Transaction` 으로 묶어서 데이터 조작
  2. ERP 데이터 조작
     이때, 문제 발생하면 1 rollback 
     문제가 발생하지 않으면 1 commit
- 하지만 문제가 발생함
  ERP 에 인터페이스를 위한 `SP` 를 6개나 호출해야했음
- 이 기능을 6가지의 실패 케이스마다 로직을 짜주는 끔찍한 일을 해야하거나
  6가지의  `SP` 를 하나의 `TX` 로 묶어서 6개중 하나라도 문제가 생기면 rollback 처리 기능이 필요했음

### 요약

- 하나의 `TX` 에서 여러개의 `SP` 을 호출 하려고 함
- 근데 `SP`중 하나에 `TX` 처리가 되어있을 경우 외부 `TX` 은 어떻게 되는지 궁금했음

---

## 결론

- 웬만한 DB는 중첩된 `TX` 를 지원함

- 중첩 `TX` 는 내부 `TX` 이 `commit` 되더라도
  
    외부 `TX` 이 최종적으로 `commit` 되기 전까지
  
    `DB` 에 반영되지 않음

- 즉 외부 `TX` 가 `rollback` 되면 내부 `TX` 의 `commit` 도 `rollback` 됨

- 단, `Autonomous Transactions` 를 사용하게 되면
  
    외부 `TX` 와 무관하게 내부 `TX` 는 DB에 반영됨