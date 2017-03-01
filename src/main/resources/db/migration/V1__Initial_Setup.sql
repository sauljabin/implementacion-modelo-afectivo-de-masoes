CREATE TABLE object (
  uuid         VARCHAR(100) NOT NULL,
  name         VARCHAR(100) NOT NULL,
  creator_name VARCHAR(100) NOT NULL
);

CREATE TABLE object_property (
  name        VARCHAR(100) NOT NULL,
  value       VARCHAR(100) NOT NULL,
  object_uuid VARCHAR(100) NOT NULL
);