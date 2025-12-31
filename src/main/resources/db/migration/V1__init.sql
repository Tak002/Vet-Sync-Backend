-- =========================
-- ENUM TYPES
-- =========================
CREATE TYPE patient_species AS ENUM (
    'DOG',
    'CAT',
    'OTHER'
    );

CREATE TYPE task_status AS ENUM (
    'PENDING',
    'IN_PROGRESS',
    'CONFIRM_WAITING',
    'COMPLETED'
    );

CREATE TYPE patient_gender AS ENUM (
    'M',
    'NM',  -- 중성화 수컷
    'F',
    'SF'   -- 중성화 암컷
    );

CREATE TYPE patient_status AS ENUM (
    'REGISTERED',
    'ADMITTED',
    'HOSPITALIZED',
    'DISCHARGED'
    );

CREATE TYPE staff_role AS ENUM (
    'CHIEF_VET',
    'VET',
    'SENIOR_TECH',
    'TECH',
    'FRONT'
    );

CREATE TYPE medical_value_type AS ENUM (
    'INTEGER',
    'FLOAT',
    'STAFF_ID',
    'RESPIRATORY_RATE'
    );

-- =========================
-- TABLES
-- =========================
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
    phone text,
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
    gender patient_gender NOT NULL,
    status patient_status NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    created_by uuid NOT NULL,
    updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE task_definitions (
    id uuid PRIMARY KEY,
    name varchar(255) NOT NULL,
    is_global boolean NOT NULL,
    hospital_id uuid,
    value_type medical_value_type NOT NULL,
    description text
);

CREATE TABLE tasks (
    id uuid PRIMARY KEY,
    hospital_id uuid NOT NULL,
    patient_id uuid NOT NULL,
    task_definition_id uuid NOT NULL,
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

CREATE TABLE patient_day_notes (
    id uuid PRIMARY KEY,
    hospital_id uuid NOT NULL,
    patient_id uuid NOT NULL,
    content text NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);

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
    ADD CONSTRAINT fk_tasks_assignee
        FOREIGN KEY (assignee_id) REFERENCES staffs (id);

ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_created_by
        FOREIGN KEY (created_by) REFERENCES staffs (id);

ALTER TABLE patient_day_notes
    ADD CONSTRAINT fk_patient_day_notes_hospital
        FOREIGN KEY (hospital_id) REFERENCES hospitals (id);

ALTER TABLE patient_day_notes
    ADD CONSTRAINT fk_patient_day_notes_patient
        FOREIGN KEY (patient_id) REFERENCES patients (id);

CREATE UNIQUE INDEX uq_owners_hospital_phone_not_null
    ON owners (hospital_id, phone)
    WHERE phone IS NOT NULL;
