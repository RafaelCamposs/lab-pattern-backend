CREATE TABLE evaluation (
  id BINARY(16) PRIMARY KEY DEFAULT (UUID()),
  submission_id CHAR(36) UNIQUE REFERENCES submission(id),
  detected_pattern_id BINARY(16) REFERENCES design_pattern(id),
  correctness_score DECIMAL(5,2), -- Exemplo: 85.00%
  style_score DECIMAL(5,2), -- Exemplo: 75.00%
  feedback TEXT,
  evaluated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);