CREATE TABLE IF NOT EXISTS tournament (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    location VARCHAR(255) NOT NULL,
    description TEXT,
    logo_url VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_by_id BIGINT NOT NULL,
    max_participants INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL,
    FOREIGN KEY (created_by_id) REFERENCES user(id) ON DELETE CASCADE
);