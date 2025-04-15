CREATE TABLE submission (
  id BINARY(16) PRIMARY KEY DEFAULT (UUID()),
  user_id BINARY(16) REFERENCES user(id),
  challenge_id BINARY(16) REFERENCES challenge(id),
  code TEXT NOT NULL,
  language VARCHAR(50) DEFAULT 'kotlin',
  submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  status ENUM('pending', 'processing', 'completed', 'error') DEFAULT 'pending'
);