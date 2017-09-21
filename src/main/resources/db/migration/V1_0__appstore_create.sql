CREATE TABLE architecture (
  id          SERIAL PRIMARY KEY,
  name        VARCHAR(30) NOT NULL,
  description TEXT,
  created    TIMESTAMP
);

CREATE TABLE category (
  id          SERIAL PRIMARY KEY,
  name        VARCHAR(30) NOT NULL,
  description TEXT,
  created    TIMESTAMP
);

CREATE TABLE license (
  id          SERIAL PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  description TEXT,
  url         VARCHAR(255),
  created    TIMESTAMP
);

CREATE TABLE os (
  id      SERIAL PRIMARY KEY,
  name    VARCHAR(30) NOT NULL DEFAULT 'MidnightBSD',
  version VARCHAR(10) NOT NULL,
  created    TIMESTAMP
);

CREATE TABLE package (
  id          SERIAL PRIMARY KEY,
  name        VARCHAR(255) NOT NULL,
  description TEXT,
  url         VARCHAR(255),
  created    TIMESTAMP
);

CREATE TABLE package_instance (
  id              SERIAL PRIMARY KEY,
  version         VARCHAR(255) NOT NULL,
  os_id           INT,
  architecture_id INT,
  package_id      INT,
  created    TIMESTAMP
);

CREATE TABLE rating (
  id         SERIAL PRIMARY KEY,
  package_id INT,
  author     VARCHAR(255),
  comment    VARCHAR(500),
  score      INT,
  created    TIMESTAMP
);