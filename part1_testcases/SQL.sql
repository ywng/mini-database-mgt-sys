CREATE TABLE Book (
	isbn varchar(20) PRIMARY KEY,
	title varchar(20),
	author varchar(20),
	pages int,
	editorial varchar(15)
);
CREATE TABLE Student (
	studentId int PRIMARY KEY,
	name varchar(15),
	gender varchar(1),
	age int
);
CREATE TABLE Vehicle (
	licenseNumber varchar(10),
	brand varchar(15),
	model varchar(15),
	type varchar(2),
	engineSize int
);
INSERT INTO Student
VALUES (10, 'John Smith', 'M', 22);

INSERT INTO Student
VALUES (11, 'Hsu You-Ting', 'F', 23);

INSERT INTO Student (name, age, studentId, gender)
VALUES ('Ai Toshiko', 21, 12, 'F');

INSERT INTO Student (age, studentId, gender, name)
VALUES (20, 13, 'M', 'Fernando Sierra');

INSERT INTO Student
VALUES (14, 'Mohammed Ali', 'M', 25);

INSERT INTO Student (studentId, name, age)
VALUES (1, 'John', 13);
INSERT INTO Student (studentId, name, age)
VALUES (2, 'Mary', 11);
INSERT INTO Student (studentId, name, age)
VALUES (3, 'Will', 12);
INSERT INTO Student (studentId, name, age)
VALUES (4, 'Sue', 13);
INSERT INTO Student (studentId, name, age)
VALUES (5, 'James', 14);
INSERT INTO Vehicle (licenseNumber, brand, model, engineSize, type)
VALUES ('AH-3044', 'Toyota', 'Corolla', 1600, 'SE');
INSERT INTO Book
VALUES ('12345', 'Romeo and Juliet', 'Shakespeare', 3, 'Prentice Hall');

