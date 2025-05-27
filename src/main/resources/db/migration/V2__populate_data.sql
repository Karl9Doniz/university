INSERT INTO lectors (name, degree, salary) VALUES
('John Smith', 'PROFESSOR',150000.00),
('Alice Johnson', 'ASSOCIATE_PROFESSOR',90000.00),
('Bob Williams', 'ASSISTANT',60000.00),
('Carol Davis','ASSISTANT',62000.00),
('Daniel Brown','PROFESSOR',140000.00);

INSERT INTO departments (name, head_id) VALUES
('Computer Science',1),
('Mathematics',5),
('Physics',1);

INSERT INTO department_lectors (department_id, lector_id) VALUES
(1,1),(1,2),(1,3),
(2,2),(2,3),(2,4),
(3,1),(3,4),(3,5);
