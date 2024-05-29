CREATE TABLE IF NOT EXISTS typewriter_facts (
    player_id UUID PRIMARY KEY,
    facts JSON
);

CREATE TABLE IF NOT EXISTS typewriter_assets (
    path VARCHAR(255) PRIMARY KEY,
    content TEXT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS typewriter_pages (
    path VARCHAR(255) PRIMARY KEY,
    content JSON,
    is_staging BOOLEAN DEFAULT FALSE
);