CREATE TABLE evaluation (
  id BINARY(16) PRIMARY KEY DEFAULT (UUID()),
  submission_id BINARY(16) UNIQUE REFERENCES submission(id),
  detected_pattern_id BINARY(16) REFERENCES design_pattern(id),
  score INTEGER,
  feedback TEXT,
  evaluated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);