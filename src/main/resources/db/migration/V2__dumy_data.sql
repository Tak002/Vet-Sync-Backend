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