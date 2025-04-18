CREATE TABLE evaluation (
  id BINARY(16) PRIMARY KEY DEFAULT (UUID()),
  submission_id BINARY(16) UNIQUE REFERENCES submission(id),
  score INTEGER,
  feedback TEXT,
  evaluated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);