- JDBC(Java Database Connectivity)는 Java에서 DB에 접속할 수 있도록 하는 Java API
- 주요 역할
	- DB 연결: 다양한 종류의 DB에 연결할 수 있는 표준화된 방법 제공
	- SQL 실행: DB에 Query를 전송하고 결과를 받아올 수 있음
	- Transaction 관리: Transaction을 시작, 커밋, 롤백할 수 있는 기능 제공
	- 결과 처리: 쿼리 결과를 자바 객체로 매핑하여 처리할 수 있게 함
- 핵심 구성 요소
	- JDBC Driver Manager: DB와 실제 연결을 담당
	- JDBC API: 어플리케이션 개발자가 사용하는 인터페이스
- 동작 과정
```java
// 1. Driver 로드
Class.forName("com.mysql.jdbc.Driver");

// 2. Connection 객체 생성
Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

// 3. Statement 객체 생성
Statement stmt = conn.createStatement();

// 4. Query 실행
ResultSet rs = stmt.executeQuery("SELECT * FROM users");

// 5. 결과 처리
while(rs.next()) {
    // 데이터 처리
}

// 6. 자원 해제
rs.close();
stmt.close();
conn.close();
```

- Class.forName() 동작 원리
	- Reflection 으로 단순히 클래스만 로드만 하지 않고 초기화까지 수행함
	- 클래스를 메모리에 로드하고 클래스의 static 블록을 실행함
	- JDBC 드라이버 클래스들은 대부분 static 초기화 블록을 포함하고 있음
	- static 블록에서 드라이버 자신을 DriverManager에 등록하는 코드가 실행됨
	- [mysql - Java Class.forName, JDBC connection loading driver - Stack Overflow](https://stackoverflow.com/questions/18058714/java-class-forname-jdbc-connection-loading-driver)
	- [[JDBC] MySQL 드라이버 Class.forName()의 비밀 - 로드만 했을 뿐인데 getConnection()이 가능하다고?](https://pjh3749.tistory.com/250)

## 주요 인터페이스
### Connection
- DB 연결을 나타냄
- Transaction 처리 메서드 제공
- Statement, PreparedStatement 객체 생성
### Statement
- 정적 SQL문을 실행할 때 사용
- SQL Injection 공격에 취약할 수 있음
### PreparedStatement
- 미리 컴파일된 SQL문을 실행할 때 사용
- SQL Injection 공격 방지
- 쿼리를 미리 컴파일하여 재사용이 가능하고 Statement보다 성능상 유리함
### ResultSet
- SQL 쿼리 결과를 저장하는 객체
- cursor를 통해 데이터 탐색

## Cursor
- 쿼리 결과의 현재 위치를 가리키는 포인터
- ResultSet 객체에서 cursor는 특정 행을 가리킴
- 데이터를 순차적으로 접근 가능
- 장점
	- 결과를 전부 메모리에 로드하지 않아서 메모리 효율적
	- 대용량 데이터 처리에 접합
	- 순차적 접근이 빠름
- 단점
	- 양방향 스크롤 시 성능 저하
	- 동시에 여러 행 접근 불가
	- 커넥션이 오래 유지되어야함

## Connection Pool
- DB 연결 미리 생성하고 관리하는 기법
- 연결 생성 비용 감소
- 어플리케이션 성능 향상
- 주요 특징
	- 연결 재사용: 미리 생성된 연결을 재사용하여 연결 생성 및 해제에 따른 오버헤드 감소
	- 성능 최적화: 데이터베이스 접근 속도 향상 및 시스템 리소스 사용 효율화
	- 연결 관리: 연결의 생성, 할당, 반환, 소멸 등을 자동으로 관리