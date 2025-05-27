CREATE TABLE lectors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    degree ENUM('ASSISTANT','ASSOCIATE_PROFESSOR','PROFESSOR') NOT NULL,
    salary DECIMAL(19,2) NOT NULL
);

CREATE TABLE departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    head_id BIGINT NOT NULL,
    INDEX (head_id),
    FOREIGN KEY (head_id) REFERENCES lectors(id)
);

CREATE TABLE department_lectors (
    department_id BIGINT NOT NULL,
    lector_id BIGINT NOT NULL,
    PRIMARY KEY (department_id, lector_id),
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (lector_id) REFERENCES lectors(id)
);
