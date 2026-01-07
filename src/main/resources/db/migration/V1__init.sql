-- PostgreSQL 17 기준
CREATE TYPE patient_species AS ENUM ('DOG','CAT','OTHER');

CREATE TYPE task_status AS ENUM ('PENDING','IN_PROGRESS','CONFIRM_WAITING','COMPLETED');

CREATE TYPE patient_gender AS ENUM (
    'M',
    'NM',  -- 중성화 수컷
    'F',
    'SF'   -- 중성화 암컷
);

CREATE TYPE patient_status AS ENUM ('REGISTERED','ADMITTED','HOSPITALIZED','DISCHARGED');

CREATE TYPE staff_role AS ENUM ('CHIEF_VET','VET','SENIOR_TECH','TECH','FRONT');

-- =========================================================
CREATE TABLE hospitals (
    id uuid PRIMARY KEY,
    name text NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE staffs (
    id uuid PRIMARY KEY,
    hospital_id uuid NOT NULL,
    login_id text NOT NULL,
    password text NOT NULL,
    name text NOT NULL,
    role staff_role NOT NULL,
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT uq_staff_login_per_hospital UNIQUE (hospital_id, login_id)
);

CREATE TABLE owners (
    id uuid PRIMARY KEY,
    hospital_id uuid NOT NULL,
    name text NOT NULL,
    phone text NOT NULL,
    email text,
    address text,
    memo text,
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT now(),
    created_by uuid NOT NULL,
    updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE patients (
    id uuid PRIMARY KEY,
    hospital_id uuid NOT NULL,
    owner_id uuid NOT NULL,
    name text NOT NULL,
    species patient_species NOT NULL,
    species_detail text,
    breed text,
    cc text,
    diagnosis text,
    gender patient_gender NOT NULL,
    status patient_status NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    created_by uuid NOT NULL,
    updated_at timestamptz NOT NULL DEFAULT now()
);

-- Task Definition: 대분류(name) + 옵션 정의(options)
CREATE TABLE task_definitions (
    id uuid PRIMARY KEY,
    name varchar(255) NOT NULL,
    is_fixed boolean NOT NULL,
    hospital_id uuid,
    description text,
    order_no integer NULL,
    options jsonb NOT NULL DEFAULT '{}'::jsonb
);

-- 공용 노트 테이블: 환자/일자/TaskDefinition 단위
-- 다중 선택을 위해 selected_option_keys smallint[] 로 저장
CREATE TABLE patient_day_task_definition_notes (
    id uuid PRIMARY KEY,
    hospital_id uuid NOT NULL,
    patient_id uuid NOT NULL,
    task_date date NOT NULL,
    task_definition_id uuid NOT NULL,
    content text NOT NULL DEFAULT '',

    selected_option_keys smallint[] NOT NULL DEFAULT '{}'::smallint[],

    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE tasks (
    id uuid PRIMARY KEY,
    hospital_id uuid NOT NULL,
    patient_id uuid NOT NULL,
    task_definition_id uuid NOT NULL,

    -- 업무 예정 일자 / 시간
    task_date date NOT NULL,               -- YYYY-MM-DD
    task_hour smallint NOT NULL,           -- 0 ~ 23

    -- 공용 노트 참조
    patient_day_task_definition_note_id uuid,

    -- 개별 task 전용 메모
    task_notes text,

    status task_status NOT NULL,
    result text,
    assignee_id uuid,
    created_at timestamptz NOT NULL DEFAULT now(),
    created_by uuid NOT NULL,
    started_at timestamptz,
    confirm_requested_at timestamptz,
    completed_at timestamptz,
    updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE patient_day_context_notes (
    id uuid PRIMARY KEY,
    hospital_id uuid NOT NULL,
    patient_id uuid NOT NULL,
    note_date date NOT NULL,
    content jsonb NOT NULL DEFAULT '{}'::jsonb,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);

-- =========================================================
-- INDEXES & CONSTRAINTS (추가 제약)
-- =========================================================
-- TaskDefinition 이름 유니크(병원 내, 공백 트림/대소문자 무시)
CREATE UNIQUE INDEX uk_task_definition_hospital_name_norm
    ON task_definitions (hospital_id, lower(btrim(name)));

-- fixed=true 이면서 order_no가 있는 경우에만 병원 내 유니크
CREATE UNIQUE INDEX uk_task_definition_fixed_order_per_hospital
    ON task_definitions (hospital_id, order_no)
    WHERE is_fixed = true AND order_no IS NOT NULL;

-- =========================
-- FOREIGN KEYS
-- =========================
ALTER TABLE staffs
    ADD CONSTRAINT fk_staffs_hospital
        FOREIGN KEY (hospital_id) REFERENCES hospitals (id);

ALTER TABLE owners
    ADD CONSTRAINT fk_owners_hospital
        FOREIGN KEY (hospital_id) REFERENCES hospitals (id);

ALTER TABLE owners
    ADD CONSTRAINT fk_owners_created_by
        FOREIGN KEY (created_by) REFERENCES staffs (id);

ALTER TABLE patients
    ADD CONSTRAINT fk_patients_hospital
        FOREIGN KEY (hospital_id) REFERENCES hospitals (id);

ALTER TABLE patients
    ADD CONSTRAINT fk_patients_owner
        FOREIGN KEY (owner_id) REFERENCES owners (id);

ALTER TABLE patients
    ADD CONSTRAINT fk_patients_created_by
        FOREIGN KEY (created_by) REFERENCES staffs (id);

ALTER TABLE task_definitions
    ADD CONSTRAINT fk_task_definitions_hospital
        FOREIGN KEY (hospital_id) REFERENCES hospitals (id);

ALTER TABLE patient_day_task_definition_notes
    ADD CONSTRAINT fk_pdtddn_hospital
        FOREIGN KEY (hospital_id) REFERENCES hospitals (id);

ALTER TABLE patient_day_task_definition_notes
    ADD CONSTRAINT fk_pdtddn_patient
        FOREIGN KEY (patient_id) REFERENCES patients (id);

ALTER TABLE patient_day_task_definition_notes
    ADD CONSTRAINT fk_pdtddn_task_definition
        FOREIGN KEY (task_definition_id) REFERENCES task_definitions (id);

ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_hospital
        FOREIGN KEY (hospital_id) REFERENCES hospitals (id);

ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_patient
        FOREIGN KEY (patient_id) REFERENCES patients (id);

ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_task_definition
        FOREIGN KEY (task_definition_id)
            REFERENCES task_definitions (id);

ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_pdtddn
        FOREIGN KEY (patient_day_task_definition_note_id)
            REFERENCES patient_day_task_definition_notes (id)
                ON DELETE SET NULL;

ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_assignee
        FOREIGN KEY (assignee_id) REFERENCES staffs (id);

ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_created_by
        FOREIGN KEY (created_by) REFERENCES staffs (id);

ALTER TABLE patient_day_context_notes
    ADD CONSTRAINT fk_patient_day_context_notes_hospital
        FOREIGN KEY (hospital_id) REFERENCES hospitals (id);

ALTER TABLE patient_day_context_notes
    ADD CONSTRAINT fk_patient_day_context_notes_patient
        FOREIGN KEY (patient_id) REFERENCES patients (id);

-- =========================================================
-- INDEXES / UNIQUE
-- =========================================================
CREATE UNIQUE INDEX uq_owners_hospital_phone_not_null
    ON owners (hospital_id, phone)
    WHERE phone IS NOT NULL;

CREATE UNIQUE INDEX uq_patient_day_context_notes_unique
    ON patient_day_context_notes (hospital_id, patient_id, note_date);

CREATE UNIQUE INDEX uq_patient_hospital_owner_name
    ON patients (hospital_id, owner_id, name);

-- 공용 노트 동일성 보장 (환자/일자/TaskDefinition 당 1개)
CREATE UNIQUE INDEX uq_pdtddn_unique
    ON patient_day_task_definition_notes (hospital_id, patient_id, task_date, task_definition_id);

CREATE UNIQUE INDEX uq_task_definition_hospital_name_norm
    ON task_definitions (hospital_id, lower(btrim(name)));

-- "그날 환자의 공용 노트 전체" 조회 최적화
CREATE INDEX ix_pdtddn_day_lookup
    ON patient_day_task_definition_notes (hospital_id, patient_id, task_date);

-- 다중 선택 조회 최적화 (예: 특정 옵션 포함 여부 검색)
CREATE INDEX ix_pdtddn_selected_option_keys_gin
    ON patient_day_task_definition_notes
        USING GIN (selected_option_keys);

-- tasks에서 note FK 조인 최적화
CREATE INDEX ix_tasks_pdtddn_id
    ON tasks (patient_day_task_definition_note_id);

CREATE INDEX ix_pdcn_day_lookup
    ON patient_day_context_notes (hospital_id, patient_id, note_date);
