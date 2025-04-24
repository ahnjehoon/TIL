- 예를들어 Order 컨텍스트와 Product 컨텍스트가 있다고 가정
- Order의 OrderItem에서 Product 정보를 참조해야할 일이 있을 수 있음
- 두 가지 접근 방법이 있음

## Context Mapping: Anti-Corruption Layer 패턴
- Order 컨텍스트에서는 Product의 일부 정보만 필요하고 원본 Product 정보의 변경에 영향을 받지 않아야 함. Order 컨텍스트 내에 Product의 표현을 별도로 만드는 것이 좋음

```java
public class ProductSnapshot {
    private final String productId;  // 원본 Product의 ID 참조
    private final String productName;
    private final Money price;
}

// OrderItem에서 사용
public class OrderItem {
    private final OrderItemId id;
    private final ProductSnapshot product;
    private final int quantity;
}

```

- 장점
	- Order 컨텍스트가 Product 컨텍스트 변경에 영향을 덜 받음
	- 주문 생성 시점의 제품 정보를 유지 (가격 변경 등 영향 없음)
	- 각 컨텍스트 경계가 명확하게 유지됨

## Shared Kernel 패턴
- 두 컨텍스트가 밀접하게 연결되어 있고 Product 도메인 객체를 있는 그대로 공유해야 하는 경우
```java
public class OrderItem {
    private final OrderItemId id;
    private final Product product;  // Product 컨텍스트의 모델을 직접 참조
    private final int quantity;
}

```

- 컨텍스트간 강한 결합도가 생김
- Product 변경이 Order에 직접적인 영향을 미칠 수 있음
- 도메인 경계가 모호해짐

---

## 결론
- Context Mapping 사용
- 참조 대상 컨텍스트에서 필요 정보만 가져와서 자체 모델로 변환해 사용
- 위처럼 할 경우
	- 각 바운디드 컨텍스트의 독립성 유지
	- 컨텍스트간 결합도가 낮아짐
	- 나중에 MSA로 분리하기 쉬움
