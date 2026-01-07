-- =========================================================
-- DUMMY DATA (초기 시드)
-- =========================================================

-- 병원
INSERT INTO hospitals (id, name)
VALUES ('3fa85f64-5717-4562-b3fc-2c963f66afa6', '행복동물병원');

-- 스태프 (기존 + 유지)
INSERT INTO staffs (
    id, hospital_id, login_id, password, name, role, is_active, created_at, updated_at
) VALUES (
    '214c43a5-5a9a-4c75-8f3f-669c10136e5a',
    '3fa85f64-5717-4562-b3fc-2c963f66afa6',
    'front01',
    '$2a$10$HiLEOQ2F/ZrnEzRwYhSyj.USWzLXu9nUXpbGxRUyscwMAGYbJhtiW',
    '홍길동',
    'CHIEF_VET',
    true,
    now(),
    now()
);

-- 오너 (기존 + 추가)
INSERT INTO owners (
    id, hospital_id, name, phone, email, address, memo, is_active, created_at, updated_at, created_by
) VALUES
    ('fd2aceff-861f-4a27-886d-133c9d687a17',
     '3fa85f64-5717-4562-b3fc-2c963f66afa6',
     '김민수',
     '01012345678',
     'minsu.kim@example.com',
     '서울특별시 강남구 테헤란로 123',
     '야간 방문 가능',
     true,
     now(),
     now(),
     '214c43a5-5a9a-4c75-8f3f-669c10136e5a'
    ),
    ('d2d5c1b2-59f7-4d5a-83ab-9d9d7b1a7c01',
     '3fa85f64-5717-4562-b3fc-2c963f66afa6',
     '이수진',
     '01098765432',
     'sujin.lee@example.com',
     '서울특별시 서초구 서초대로 456',
     '특이사항 없음',
     true,
     now(),
     now(),
     '214c43a5-5a9a-4c75-8f3f-669c10136e5a'
    );

-- 환자 (기존 + 추가)
INSERT INTO patients (
    id, hospital_id, owner_id, name, species, species_detail, breed, gender, status, created_by, created_at, updated_at
) VALUES
    ('e2da4625-7158-496f-9f84-9d26b7086ef2',
     '3fa85f64-5717-4562-b3fc-2c963f66afa6',
     'fd2aceff-861f-4a27-886d-133c9d687a17',
     '초코',
     'DOG',
     '포메라니안',
     '포메라니안',
     'M',
     'REGISTERED',
     '214c43a5-5a9a-4c75-8f3f-669c10136e5a',
     now(),
     now()
    ),
    ('a8b3f1d9-4e65-4c10-9a3f-9f2ab2b2c3d4',
     '3fa85f64-5717-4562-b3fc-2c963f66afa6',
     'd2d5c1b2-59f7-4d5a-83ab-9d9d7b1a7c01',
     '밤비',
     'CAT',
     '코리안 숏헤어',
     'KSH',
     'F',
     'REGISTERED',
     '214c43a5-5a9a-4c75-8f3f-669c10136e5a',
     now(),
     now()
    );

-- Task Definitions (fixed: order_no 1..N)
-- 지정된 순서: 체중, 체온, 혈압, 심박수, 호흡수, Vomit, Feces, Urine
INSERT INTO task_definitions (id, name, is_fixed, hospital_id, description, order_no, options) VALUES
    ('11111111-1111-1111-1111-111111111111', '체중',   true, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '환자의 체중 측정 (kg)',           1, '{}'::jsonb),
    ('11111111-1111-1111-1111-111111111112', '체온',   true, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '환자의 체온 측정 (℃)',           2, '{}'::jsonb),
    ('11111111-1111-1111-1111-111111111114', '혈압',   true, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '혈압 수치',                      3, '{}'::jsonb),
    ('11111111-1111-1111-1111-111111111113', '심박수', true, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '분당 심박수',                    4, '{}'::jsonb),
    ('11111111-1111-1111-1111-111111111118', '호흡수', true, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '호흡수(RR / SRR)',               5, '{"1":"cough","2":"맑은콧물","3":"화농성콧물","4":"crackle"}'::jsonb),
    ('11111111-1111-1111-1111-111111111115', 'Vomit',  true, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '구토 횟수',                      6, '{"1":"위액","2":"음식물","3":"혈액","4":"거품","5":"시간당2회이상"}'::jsonb),
    ('11111111-1111-1111-1111-111111111116', 'Feces',  true, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '배변 횟수',                      7, '{"1":"설사","2":"연변","3":"정상","4":"혈액","5":"점액","6":"흑변"}'::jsonb),
    ('11111111-1111-1111-1111-111111111117', 'Urine',  true, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '배뇨 횟수',                      8, '{"1":"갈색뇨","2":"혈뇨","3":"정상","4":"황달뇨"}'::jsonb),
    ('11111111-1111-1111-1111-111111111119', 'Fluids', true, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '수액 투여량 (ml)',               9, '{}'::jsonb);

-- 비고정 항목 (order_no 없음)
INSERT INTO task_definitions (id, name, is_fixed, hospital_id, description, options) VALUES
    ('22222222-2222-2222-2222-222222222221', '심장약',  false, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '심장약 투약 담당자', '{}'::jsonb),
    ('22222222-2222-2222-2222-222222222222', '링거독',  false, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '링거 수액 투여량', '{}'::jsonb),
    ('22222222-2222-2222-2222-222222222223', '흉방',    false, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '흉강 천자 시술 담당자', '{}'::jsonb),
    ('22222222-2222-2222-2222-222222222224', '전해질',  false, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '전해질 보정 투여량', '{}'::jsonb),
    ('22222222-2222-2222-2222-222222222225', 'X-ray',   false, '3fa85f64-5717-4562-b3fc-2c963f66afa6', '방사선 촬영', '{}'::jsonb);