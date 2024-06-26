## 오류 발생

- 다음과 같은 코드에서 문제가 발생함
  - **sampleRepository.save(createTarget);**
- 다음처럼 수정하니 문제가 발생하지 않았음
  - **createTarget = sampleRepository.save(createTarget);**
- 두 코드 모두 아래에서 createTarget을 다른 관계있는 테이블에서 사용하긴 함

## 원인

- @GeneratedValue 로 선언되지 않은 PK의 경우 DB에 insert 쿼리를 날림
- 하지만 @Id 만 선언되어 있는 경우 DB에 해당 Key값이 있는지 Select 쿼리만 날림
- 이때, 데이터가 없을 경우 새로운 인스턴스를 생성하고,
  새롭게 생성한 인스턴스를 영속성 컨텍스트에서 관리함(왜….)
- 영속성컨텍스트에서 관리되는 객체, 사용자가 생성한 객체 두개가 생겨버리는 것임
- 이때 사용자가 생성한 객체를 다른 엔티티 생성시 넣으려 하면
  영속성 컨텍스트 객체 vs 사용자 객체 간 자강두천함
- 왜 이렇게 설계했는지 모르겠는데 위와 같은 상황때문에 그럼

## 해결방법

- 위에도 써 놓았듯 Key를 DB에서 생성하지 않고 생성하는 경우라면 
  다음과 같이 작성하면됨
  
  - createTarget = sampleRepository.save(createTarget);


