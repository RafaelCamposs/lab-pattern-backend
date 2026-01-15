INSERT INTO user (id, name, email, password, created_at, updated_at, deleted_at)
VALUES
    (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), 'Test User 1', 'test1@example.com', '$2a$10$rZqT9k4W3eJNQz5dKQqHUe8F9J4yMZXjXL5kQ4B9Y4HZC9Q5B9Y4H', NOW(), NOW(), NULL),
    (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), 'Test User 2', 'test2@example.com', '$2a$10$rZqT9k4W3eJNQz5dKQqHUe8F9J4yMZXjXL5kQ4B9Y4HZC9Q5B9Y4H', NOW(), NOW(), NULL);

INSERT INTO challenge (id, title, description, expected_pattern_id, published_at, created_at, is_daily)
SELECT
    UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001', '-', '')) as id,
    'Test Challenge 1' as title,
    'Description for test challenge 1' as description,
    dp.id as expected_pattern_id,
    NOW() as published_at,
    NOW() as created_at,
    TRUE as is_daily
FROM design_pattern dp
WHERE dp.name = 'Singleton'
LIMIT 1;

INSERT INTO challenge (id, title, description, expected_pattern_id, published_at, created_at, is_daily)
SELECT
    UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440002', '-', '')) as id,
    'Test Challenge 2' as title,
    'Description for test challenge 2' as description,
    dp.id as expected_pattern_id,
    NOW() - INTERVAL 1 DAY as published_at,
    NOW() - INTERVAL 1 DAY as created_at,
    FALSE as is_daily
FROM design_pattern dp
WHERE dp.name = 'Factory Method'
LIMIT 1;

INSERT INTO submission (id, user_id, challenge_id, code, language, pattern_id, submitted_at)
SELECT
    UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440001', '-', '')) as id,
    UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')) as user_id,
    UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001', '-', '')) as challenge_id,
    'public class Singleton { private static Singleton instance; }' as code,
    'java' as language,
    dp.id as pattern_id,
    NOW() as submitted_at
FROM design_pattern dp
WHERE dp.name = 'Singleton'
LIMIT 1;

-- Insert test evaluations
INSERT INTO evaluation (id, submission_id, score, feedback, evaluated_at)
VALUES (
    UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440001', '-', '')),
    UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440001', '-', '')),
    85,
    'Good implementation',
    NOW()
);


INSERT INTO user_progress (user_id, total_score, challenges_completed, last_submission_at)
VALUES (
    UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')),
    85,
    1,
    NOW()
);
