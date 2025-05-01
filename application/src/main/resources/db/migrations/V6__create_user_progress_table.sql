CREATE TABLE user_progress (
  user_id BINARY(16) PRIMARY KEY,
  total_score INT DEFAULT 0,
  challenges_completed INT DEFAULT 0,
  last_submission_at TIMESTAMP
);