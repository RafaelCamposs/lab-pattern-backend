CREATE TABLE submission (
  id BINARY(16) PRIMARY KEY DEFAULT (UUID()),
  user_id BINARY(16) REFERENCES user(id),
  challenge_id BINARY(16) REFERENCES challenge(id),
  code TEXT NOT NULL,
  language VARCHAR(50),
  pattern_id BINARY(16) REFERENCES design_pattern(id),
  submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);