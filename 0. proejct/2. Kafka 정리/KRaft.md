## ZooKeeper의 한계점
- 기존 Kafka는 메타데이터 관리를 위해 ZooKeeper에 의존했으나 다음과 같은 문제들이 있었음
- 문제점
	- 운영 복잡성 증가: Kafka, ZooKeeper 두 시스템 모두 관리해야함
	- 장애시 복구의 복잡성: ZooKeeper 장애 시 전체 클러스터에 영향. ZooKeeper도 이원화 해야함
	- 성능 병목 현상: ZooKeeper가 메타데이터 읽기/쓰기 작업에서 제약 요소로 작용
	- 확장성 제한: 대규모 파티션 환경에서 ZooKeeper의 성능 한계

## 버전별 지원 현황
- Kafka 2.8 (2021년): Early Access 모드로 최초 도입, 프로덕션 사용 금지
- Kafka 3.0: Preview 모드로 제공, 여전히 프로덕션 사용 권장하지 않음
- Kafka 3.3 (2022년): 프로덕션 준비 완료로 공식 발표
- Kafka 3.5+: 신규 클러스터의 기본 옵션으로 자리 잡음
- Kafka 3.6+: ZooKeeper에서 KRaft로의 무중단 마이그레이션 지원
- 향후 계획
	- Kafka 4.0: ZooKeeper 모드 완전 제거, KRaft 모드만 지원 예정

## 개선 사항
- 단일 스택 배포: ZooKeeper 없이 Kafka 만으로 완전한 클러스터 구성
- 메타데이터 처리량 향상: 메타데이터 관리 성능 개선
- 빠른 리더 선출: 기존 방식보다 신속한 장애 복구
- 확장성 개선: 수백만개의 파티션으로 확장 가능

--- 

## 역할 분리

### Controller
- 메타데이터 관련 요청을 관리하는 역할을 담당
- 클러스터 메타데이터 관리: 토픽, 파티션, ISR(In Sync Replicas), 설정 등의 정보 관리
- 리더 선출: 파티션 리더 브로커 선정 및 관리
- 브로커 상태 모니터링: 활성 브로커 목록 및 상태 추적
- 메타데이터 로그 관리: `__cluster_metadata` 토픽을 통한 클러스터 상태 동기화

### Broker
- 데이터 관련 요청을 처리하는 역할을 담당
- 프로듀서와 컨슈머의 요청 처리: 메세지 읽기/쓰기 작업 수행
- 파티션 데이터 저장: 실제 토픽 데이터를 디스크에 저장하고 관리
- 복제본 관리: 팔로워 복제본으로서 리더로부터 데이터 복제
- 클라이언트 연결 처리: 프로듀서/컨슈머와의 네트워크 통신 관리

---

## 운영 모드 관련
- Contrller와 Broker 같이 사용하는 Combined 모드를 사용할 수 있음
- 하지만 프로덕션 레벨에서는 컨트롤러와 브로커를 별도로 분리하는 isolated 모드를 권장함
- Combined 모드 사용시 컨트롤러와 브로커가 동일한 JVM 프로세스 내에서 실행되어, 리소스 경쟁으로 시스템 안전성이 감소될 수 있다고 함
- 근데 3~5개노드정도는 combined 모드 써도 무방할듯

---

## 기타

### 클러스터 ID 생성 관련
- 클러스터 ID가 필수적임
- 브라우저 열고 개발자 도구에서 다음과 같이 만들 수 있음
```javascript
// 간단한 UUID 생성 후 변환
function generateClusterId() {
    const uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0;
        const v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
    
    const cleanUuid = uuid.replace(/-/g, '');
    const bytes = [];
    for (let i = 0; i < cleanUuid.length; i += 2) {
        bytes.push(parseInt(cleanUuid.substr(i, 2), 16));
    }
    
    return btoa(String.fromCharCode.apply(null, bytes)).substring(0, 22);
}

console.log(generateClusterId());
```

