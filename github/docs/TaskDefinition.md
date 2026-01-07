### 개요
- TaskDefinition은 병원별로 정의되는 업무 템플릿입니다.
- 항목은 고정(fixed=true)과 비고정(fixed=false)로 나뉩니다.
- 고정 항목만 표시 순서(order)를 가지며, 비고정 항목은 order=null 입니다.

### 데이터 모델 요약
- 엔티티: TaskDefinition
  - id: UUID
  - name: String (병원 내 유니크: 트리밍 + 대소문자 무시 기준)
  - fixed: boolean
  - order: Integer (DB 컬럼: order_no, 고정 항목만 1..N)
  - description: String
  - hospital: Hospital
  - options: Map<String, String> — JSONB (예: {"1":"위액","2":"음식물"} 형태, 숫자키-문자값)

### 유효성 및 제약
- 이름 중복
  - 앱 레벨: existsByHospital_IdAndNameIgnoreCase(...)로 사전 검증, 저장 시 name.trim() 적용
  - DB 레벨: 기능(표현식) 인덱스로 유니크 강제 — uk_task_definition_hospital_name_norm on (hospital_id, lower(btrim(name)))
- 고정 항목 순서 유일성
  - DB 레벨: 부분 유니크 인덱스 — uk_task_definition_fixed_order_per_hospital on (hospital_id, order_no) where is_fixed=true and order_no is not null
- options 검증 (@ValidOptionMap)
  - 키: ^\d+$ 숫자 문자열만 허용 (예: "1", "2")
  - 값: null/빈문자/공백 불가
  - 맵 자체는 빈 맵 {} 허용, null 불가
- DTO 제약
  - TaskDefinitionCreateRequest
    - name @NotBlank, fixed @NotNull, options @NotNull @ValidOptionMap, order @Min(1) (옵션)
  - TaskDefinitionUpdateRequest
    - 동일 제약. fixed=false일 때 전달된 order는 무시/오류 처리(서비스 검증)

### 조회/정렬 규칙
- 목록 API: GET /task-definitions
  - 정렬: 고정 항목(fixed=true)을 먼저 order ASC로 정렬 → 그 다음 비고정 항목(fixed=false)은 name ASC
  - 응답 DTO: TaskDefinitionResponse에 order 포함(비고정은 null)

### 생성/수정/삭제 API
- 생성: POST /task-definitions
  - fixed=false → order 무시(null 저장)
  - fixed=true → order 미지정 시 맨 뒤(MAX+1), 지정 시 해당 위치에 삽입
- 수정: PUT /task-definitions/{id}
  - 이름/설명/options 변경 가능
  - fixed 토글 및 order 변경 가능(아래 “순서 조정” 규칙 적용)
- 삭제: DELETE /task-definitions/{id}
  - 고정 항목 삭제 시 뒤 항목들의 order를 모두 앞으로 당겨 연속성 유지

모든 API는 @HospitalId 헤더 리졸버로 병원 범위를 강제합니다.

### 순서 조정 정책(핵심)
- 순서 스케일: 1..N의 연속 정수 유지
- 고정 항목만 순서를 가짐. 비고정은 항상 order=null
- 유니크 충돌 방지 알고리즘(부분 유니크 인덱스 고려)
  - 원칙: “일괄 UPDATE(범위 +1/-1)”는 중간에 중복키를 만들어 충돌하므로 사용하지 않음
  - 대신, “순차 개별 업데이트 + 단계별 flush()”로 처리

#### 구체 알고리즘
- 생성(fixed=true, 삽입)
  1) 목표 위치 Y 계산(null/범위밖이면 MAX+1)
  2) Y..MAX 구간을 “내림차순”으로 순차 +1 밀기 → flush()
  3) 새 엔티티 order=Y 설정 → flush()

- 수정: true → true(순서 이동: X→Y)
  1) 대상 엔티티 order=null 설정 → flush()
  2) X<Y: (X<o≤Y) 구간을 “오름차순”으로 각 항목 o-1
     X>Y: (Y≤o<X) 구간을 “내림차순”으로 각 항목 o+1 → 처리 후 flush()
  3) 대상 엔티티 order=Y 설정 → flush()

- 토글: true → false
  1) 대상의 order를 비우고(null) → flush()
  2) (beforeOrder+1..MAX) 구간을 “오름차순”으로 각 항목 -1 → flush()

- 토글: false → true
  1) 목표 위치 Y 계산(null/범위밖이면 MAX+1)
  2) 대상 order=null 설정 → flush()
  3) Y..MAX 구간을 “내림차순”으로 각 항목 +1 → flush()
  4) 대상 order=Y 설정 → flush()

- 삭제(true 항목)
  1) 삭제 이전 order 기준으로 (beforeOrder+1..MAX)를 “오름차순”으로 각 항목 -1 → flush()

위와 같이 단계마다 명시적 flush()를 통해 DB 적용 순서를 보장하여 부분 유니크 인덱스 충돌을 회피합니다.

### 에러 및 예외 매핑
- 공통
  - HOSPITAL_NOT_FOUND: 병원 없음 → 404
  - TASK_DEFINITION_NOT_FOUND: 항목 없음 → 404
  - INVALID_INPUT_VALUE: 요청 값 오류(예: fixed=false인데 order 지정) → 400
  - ENTITY_ALREADY_EXISTS: 동일 병원 내 이름 중복(트리밍/대소문자 무시) → 409
- DB 유니크 위반 시(예: 동시성): DataIntegrityViolationException → 409로 매핑 가능(전역 예외 처리)

### 예시 요청/응답
- 생성(고정, 2번 위치 삽입)
```
POST /task-definitions
{
  "name": "체중",
  "fixed": true,
  "description": "환자 체중 측정",
  "options": {"1":"위액","2":"음식물"},
  "order": 2
}
```

- 수정(고정 항목을 4번으로 이동)
```
PUT /task-definitions/{id}
{
  "name": "체중",
  "fixed": true,
  "description": "환자 체중 측정",
  "options": {"1":"위액","2":"음식물"},
  "order": 4
}
```

- 토글(true → false)
```
PUT /task-definitions/{id}
{
  "name": "체중",
  "fixed": false,
  "description": "환자 체중 측정",
  "options": {"1":"위액","2":"음식물"}
}
```

### DB/마이그레이션 요약
- V1__init.sql
  - 전체 스키마 통합, task_definitions.order_no 포함
  - 인덱스
    - 이름 유니크(기능 인덱스): uk_task_definition_hospital_name_norm on (hospital_id, lower(btrim(name)))
    - 고정 순서 유니크(부분 인덱스): uk_task_definition_fixed_order_per_hospital on (hospital_id, order_no) where is_fixed=true and order_no is not null
- V2__dummy_data.sql
  - 기본 데이터 및 고정 항목의 초기 순서 시드(체중, 체온, 혈압, 심박수, 호흡수, Vomit, Feces, Urine, Fluids)

### 기타 주의사항/권장사항
- 동시성 높은 환경에서는 병원 단위 또는 구간 단위 비관적 락(PESSIMISTIC_WRITE) 또는 재시도 정책을 추가하면 더 안전합니다.
- 대량 재정렬이 빈번한 경우, 간격 정렬(예: 10,20,30 …)을 도입하고 주기적으로 컴팩션(1..N)하는 설계도 고려할 수 있습니다.
- Swagger에 400/409 응답 예시와 순서 조정 정책을 문서화하면 클라이언트 통합이 쉬워집니다.
