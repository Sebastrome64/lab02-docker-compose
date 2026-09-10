CREATE TABLE IF NOT EXISTS health_check (
                                            id SERIAL PRIMARY KEY,
                                            checked_at TIMESTAMP DEFAULT NOW()
    );

INSERT INTO health_check (checked_at) VALUES (NOW());