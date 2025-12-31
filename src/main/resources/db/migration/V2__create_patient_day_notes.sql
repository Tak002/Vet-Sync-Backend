-- =========================
-- PATIENT DAY NOTES
-- =========================
CREATE TABLE patient_day_notes (
                                   id uuid PRIMARY KEY,
                                   hospital_id uuid NOT NULL,
                                   patient_id uuid NOT NULL,
                                   content text NOT NULL,
                                   created_at timestamptz NOT NULL DEFAULT now(),
                                   updated_at timestamptz NOT NULL DEFAULT now()
);

ALTER TABLE patient_day_notes
    ADD CONSTRAINT fk_patient_day_notes_hospital
        FOREIGN KEY (hospital_id) REFERENCES hospitals (id);

ALTER TABLE patient_day_notes
    ADD CONSTRAINT fk_patient_day_notes_patient
        FOREIGN KEY (patient_id) REFERENCES patients (id);
