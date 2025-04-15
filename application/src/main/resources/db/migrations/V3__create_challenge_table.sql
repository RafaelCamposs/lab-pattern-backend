CREATE TABLE challenge (
  id BINARY(16) PRIMARY KEY DEFAULT (UUID()),
  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  expected_pattern_id BINARY(16) REFERENCES design_pattern(id),
  published_at DATE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);