- N + 1 문제를 해결하기 위한 방법중 하나로 batchSize 가 있음
- 여태까지 자식들의 데이터 갯수를 제한하는 방법으로 알고 있었는데 잘못알고 있어 재정리함
~~(기존에는 100개 가져오면 batchsize 만큼의 자식 데이터를 가져오는 것으로 알고 있었음)~~

## 예시
- A, B 테이블 두개가 있고 B는 A의 자식임을 가정함 A (1) ──< (N) B
- A 하나당 B가 1000개씩 존재
- 한번에 A를 100개씩 가져온다고 가정함

```java
@Entity
class A {
  @Id Long id;

  @OneToMany(mappedBy = "a", fetch = FetchType.LAZY)
  // case2에서만 활성화: @BatchSize(size = 10) 또는 글로벌 default_batch_fetch_size=10
  List<B> bList = new ArrayList<>();
}

@Entity
class B {
  @Id Long id;
  @ManyToOne(fetch = FetchType.LAZY) A a;
}

// 사용 코드(둘 다 동일)
List<A> as = em.createQuery("select a from A a order by a.id", A.class)
               .setMaxResults(100)
               .getResultList();

for (A a : as) {
  // 컬렉션 접근(초기화 트리거)
  for (B b : a.getBList()) {
    // do something
  }
}

```

### batchSize 설정 안함 (전형적인 N+1)

1. 부모 100건 로드
```sql
select a.* 
from A a 
order by a.id 
limit 100;
```

2. 컬렉션 접근할 때마다 해당 부모의 자식만 별도로 조회 → 부모 수만큼 반복(100번)
```sql
-- A id=1의 자식
select b.* from B b where b.a_id = ?;  -- 약 1,000행

-- A id=2의 자식
select b.* from B b where b.a_id = ?;  -- 약 1,000행

-- ...
-- A id=100의 자식
select b.* from B b where b.a_id = ?;  -- 약 1,000행
```

쿼리 수: 1(부모) + 100(자식) = 101개

### batchSize 10
- `@BatchSize` 또는 `hibernate.default_batch_fetch_size=10`

1. 부모 100건 로드(동일)
```sql
select a.* 
from A a 
order by a.id 
limit 100;
```

2. 첫 컬렉션 초기화 시점에, 영속성 컨텍스트에 로드되어 있으나 아직 초기화되지 않은 A의 컬렉션들 중 최대 10개를 IN으로 묶어서 한 번에 로드
```sql
-- (1차 배치) A id in (1..10)의 자식들을 "한 번에"
select b.* 
from B b 
where b.a_id in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);  -- 최대 10개 A id
-- 반환 로우 ≈ 10 × 1,000 = 10,000행

-- (2차 배치) A id in (11..20)
select b.* from B b where b.a_id in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

-- ...
-- (10차 배치) A id in (91..100)
select b.* from B b where b.a_id in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

```

쿼리 수: 1(부모) + 10(자식 배치) = 11개
