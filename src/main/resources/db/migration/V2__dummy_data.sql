INSERT INTO hospitals (id, name)
VALUES (
    '3fa85f64-5717-4562-b3fc-2c963f66afa6',
    '행복동물병원'
);

INSERT INTO staffs (
    id,
    hospital_id,
    login_id,
    password,
    name,
    role,
    is_active,
    created_at,
    updated_at
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
INSERT INTO owners (
    id,
    hospital_id,
    name,
    phone,
    email,
    address,
    memo,
    is_active,
    created_at,
    updated_at,
    created_by
) VALUES (
             'fd2aceff-861f-4a27-886d-133c9d687a17',
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
         );
INSERT INTO patients (
    id,
    hospital_id,
    owner_id,
    name,
    species,
    species_detail,
    breed,
    gender,
    status,
    created_by,
    created_at,
    updated_at
) VALUES (
             'e2da4625-7158-496f-9f84-9d26b7086ef2',
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
         );

INSERT INTO task_definitions (id, name, is_global, hospital_id, value_type, description)
VALUES
-- Vital Signs
('11111111-1111-1111-1111-111111111111', '체중', true, NULL, 'FLOAT', '환자의 체중 측정 (kg)'),
('11111111-1111-1111-1111-111111111112', '체온', true, NULL, 'FLOAT', '환자의 체온 측정 (℃)'),
('11111111-1111-1111-1111-111111111113', '심박수', true, NULL, 'INTEGER', '분당 심박수'),
('11111111-1111-1111-1111-111111111114', '혈압', true, NULL, 'INTEGER', '혈압 수치'),

-- Observation
('11111111-1111-1111-1111-111111111115', 'Vomit', true, NULL, 'INTEGER', '구토 횟수'),
('11111111-1111-1111-1111-111111111116', 'Feces', true, NULL, 'INTEGER', '배변 횟수'),
('11111111-1111-1111-1111-111111111117', 'Urine', true, NULL, 'INTEGER', '배뇨 횟수'),

-- Respiratory
('11111111-1111-1111-1111-111111111118', '호흡수', true, NULL, 'RESPIRATORY_RATE', '호흡수(RR / SRR)'),

-- Treatment
('11111111-1111-1111-1111-111111111119', 'Fluids', true, NULL, 'FLOAT', '수액 투여량 (ml)');


INSERT INTO task_definitions (id, name, is_global, hospital_id, value_type, description)
VALUES
    ('22222222-2222-2222-2222-222222222221', '심장약', false, '3fa85f64-5717-4562-b3fc-2c963f66afa6', 'STAFF_ID', '심장약 투약 담당자'),
    ('22222222-2222-2222-2222-222222222222', '링거독', false, '3fa85f64-5717-4562-b3fc-2c963f66afa6', 'FLOAT', '링거 수액 투여량'),
    ('22222222-2222-2222-2222-222222222223', '흉방', false, '3fa85f64-5717-4562-b3fc-2c963f66afa6', 'STAFF_ID', '흉강 천자 시술 담당자'),
    ('22222222-2222-2222-2222-222222222224', '전해질', false, '3fa85f64-5717-4562-b3fc-2c963f66afa6', 'FLOAT', '전해질 보정 투여량');
