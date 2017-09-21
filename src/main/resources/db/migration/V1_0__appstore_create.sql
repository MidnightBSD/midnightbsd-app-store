CREATE TABLE architecture (
  id          SERIAL PRIMARY KEY,
  name        VARCHAR(30) NOT NULL,
  description TEXT,
  created     TIMESTAMP
);

CREATE TABLE category (
  id          SERIAL PRIMARY KEY,
  name        VARCHAR(30) NOT NULL,
  description TEXT,
  created     TIMESTAMP
);

CREATE TABLE license (
  id          SERIAL PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  description TEXT,
  url         VARCHAR(255),
  created     TIMESTAMP
);

CREATE TABLE os (
  id      SERIAL PRIMARY KEY,
  name    VARCHAR(30) NOT NULL DEFAULT 'MidnightBSD',
  version VARCHAR(10) NOT NULL,
  created TIMESTAMP
);

CREATE TABLE package (
  id          SERIAL PRIMARY KEY,
  name        VARCHAR(255) NOT NULL,
  description TEXT,
  url         VARCHAR(255),
  created     TIMESTAMP
);

CREATE TABLE package_instance (
  id              SERIAL PRIMARY KEY,
  version         VARCHAR(255) NOT NULL,
  os_id           INT REFERENCES os (id),
  architecture_id INT REFERENCES architecture (id),
  package_id      INT REFERENCES package (id),
  created         TIMESTAMP
);

CREATE TABLE rating (
  id         SERIAL PRIMARY KEY,
  package_id INT REFERENCES package (id),
  author     VARCHAR(255),
  comment    VARCHAR(500),
  score      INT,
  created    TIMESTAMP
);

CREATE TABLE package_category_map (
  package_id  INT REFERENCES package (id),
  category_id INT REFERENCES category (id)
);

CREATE TABLE package_instance_license_map (
  packge_instance_id INT REFERENCES package_instance (id),
  license_id         INT REFERENCES license (id)
);