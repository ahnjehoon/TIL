# spring flyway migration

생성일: 2024년 4월 11일 오전 10:05

## 개요

- 개발 하다보면 테이블의 초기 설계가 유지되는 경우가 드뭄
- 요구사항을 하나 둘 받다보면 테이블 수정이 불가피 하게 일어남
- 이 때 Entity class 만 수정하고 배포 환경(개발 또는 운영 등..) DB에
  테이블 수정사항을 반영하지 않았을 때 문제가 발생하게 됨
  ~~(local 에서는 돌아갔는데 왜 안되지!?를 볼 수있음)~~
- 위와 같이 실수를 한 상태에서 CI/CD가 설정되어 있는 경우라면
  보통 빌드는 성공하고 배포될때 문제가 발생하게 됨
- 위 문제를 해결하기 위해 DB 형상 관리가 필요함

---

## 개발환경

- Spring boot 3.2.5
- flyway 9.22.3
  - core
  - mysql
- mariadb

---

## flyway naming convention

- [Prefix][Version][Separator][Description][Suffix]
  
  - 예시
    - V0__Initialize.sql
    - V0.1__create_new_table.sql
    - U0.1__drop_new_table.sql

- Prefix
  
  - V(version)
    - 현재 버전을 새로운 버전으로 업그레이드
  - U(Undo)
    - 현재 버전을 이전 버전으로 되돌림
  - R(Repeatable)
    - 버전에 관계 없이 매번 실행

- Version
  
  - Unique 하게 작성되어야함
  - 양식은 자유지만 논리적으로 정렬되어야함

- Separator
  
  - __ 언더바 두개 고정

- Description
  
  - _ 단어 구분은 언더바 한개

- Suffix
  
  - .sql

- convention을 강제하려면 다음 옵션 추가 필요함
  
  - *flyway.validateMigrationNaming=true*
  - Description쪽은 안먹는듯

---

## Flyway 활성화

- build.gradle
  mariadb를 사용할 것이므로 mysql 추가함

```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.flywaydb:flyway-core'
    implementation 'org.flywaydb:flyway-mysql'
    runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
    annotationProcessor 'org.projectlombok:lombok'
}
```

- application.properties

```
# 기본값이 true 지만 명시적으로 적어줌
spring.flyway.enabled=true
# history 테이블이 없는 경우 생성
spring.flyway.baseline-on-migrate=true
```

---

### 테이블 변경감지

<aside>
💡 테이블 삭제 감지는 되지 않음

</aside>

<aside>
💡 컬럼 사이즈 변경 감지 안됨

</aside>

- 변경관련 스크립트를 추출하기 위해 application.properties에 다음 옵션 추가

```
spring.jpa.properties.jakarta.persistence.schema-generation.scripts.action=update
spring.jpa.properties.jakarta.persistence.schema-generation.scripts.create-target=src/main/resources/db/migration/need_reflect_db_script.sql
```

- flyway로만 db 변경내역을 반영할 것이므로 ddlauto none 처리

```
spring.jpa.properties.hibernate.hbm2ddl.auto=none
```

---

## 테스트

- 위 설정대로 진행하면 변경사항이 감지 되면 `need_reflect_db_script.sql` 에 스크립트가 생성될것임
  단, 테이블 삭제나 컬럼 사이즈 변경이벤트 감지는 안되니 주의

- sensor, sensorData, location 테이블을 만들어볼것임

- 테이블 수정시 어떤 변경사항이 일어나는지 확인할 것이므로 Location 은 나중에 생성

- 위의 설정이 전부 되어 있다고 가정한다음 테스트 진행
1. Sensor.class 생성
   
   ```java
   @Entity
   public class Sensor {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Integer id;
       private String name;
       private String type;
       private Instant createdOn;
       @Column(length = 100)
       private String createdBy;
       private Instant updatedOn;
       @Column(length = 100)
       private String updatedBy;
   }
   ```

2. SensorData.class 생성
   
   ```java
   @Entity
   public class SensorData {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     private Instant tm;
     private Double vl;
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "sensorId", nullable = false, foreignKey = @ForeignKey(name = "FK_SENSOR_DATA__SENSOR_ID"))
     private Sensor sensor;
   }
   ```

3. gradle build
   
    `need_reflect_db_script.sql` 파일 생성
   
   <aside>
    💡 db/migration 에 아무것도 생성되지 않는다면 gradle clean 하고 재빌드
   
   </aside>

4. `need_reflect_db_script.sql` 의 파일명을  `V0__Initialize.sql` 로 변경
   한줄로 쭉 나오는게 싫다면 application.properties에 `spring.jpa.properties.hibernate.format_sql=true` 추가
   
   ```sql
   create table sensor (id integer not null auto_increment, created_by varchar(100), created_on datetime(6), name varchar(255), type varchar(255), updated_by varchar(100), updated_on datetime(6), primary key (id)) engine=InnoDB;
   create table sensor_data (id bigint not null auto_increment, tm datetime(6), vl float(53), sensor_id integer not null, primary key (id)) engine=InnoDB;
   alter table if exists sensor_data add constraint FK_SENSOR_DATA__SENSOR_ID foreign key (sensor_id) references sensor (id);
   ```

5. Application run
   sensor , sensor_data, flyway_schema_history 테이블이 생성됨

![Untitled](.\images\spring%20flyway%20migration1.png)

1. Location.class 생성
   
   ```java
   @Entity
   public class Location {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Integer id;
       private String name;
   }
   ```

2. Sensor.class 수정
   
   ```java
   @Entity
   public class Sensor {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Integer id;
   
       ...
   
       @ManyToOne
       @JoinColumn(name = "locationId")
       private Location location;
   }
   ```

3. gradle build
   
    `need_reflect_db_script.sql` 파일 생성

4. `need_reflect_db_script.sql` 의 파일명을 `V0.1__create_location_table.sql` 로 변경

```sql
create table location (id integer not null auto_increment, name varchar(255), primary key (id)) engine=InnoDB;
alter table if exists sensor add column location_id integer;
alter table if exists sensor add constraint FKfrcgwt86n3lemfu3ketsx4x71 foreign key (location_id) references location (id);
```

1. Application run
   
    location 테이블이 생성되고 sensor 테이블에 location_id 컬럼이 추가됨
   
    flyway_schema_history 테이블에 0.1v 데이터가 추가됨

![Untitled](.\images\spring%20flyway%20migration2.png)

---

## 참조

[Database Migrations with Flyway | Baeldung](https://www.baeldung.com/database-migrations-with-flyway) 

[Flyway: Naming Patterns Matter | Redgate (red-gate.com)](https://www.red-gate.com/blog/database-devops/flyway-naming-patterns-matter)