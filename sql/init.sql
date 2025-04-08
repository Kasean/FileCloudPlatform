CREATE USER core_user WITH PASSWORD 'sd$2sq1K52Fa';

CREATE DATABASE cloudplatform OWNER core_user;

\c cloudplatform;

SET ROLE core_user;

CREATE TABLE users(
                      id SERIAL PRIMARY KEY NOT NULL,
                      name VARCHAR(50) NOT NULL,
                      password VARCHAR(100) NOT NULL,
                      email VARCHAR(100) NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

GRANT ALL PRIVILEGES ON DATABASE cloudplatform TO core_user;