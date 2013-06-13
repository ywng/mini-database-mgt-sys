
CREATE TABLE Vehicle (
	licenseNumber varchar(10),
	brand varchar(15),
	model varchar(15),
	type varchar(2),
	engineSize int
);

CREATE TABLE Book (
	isbn varchar(20) PRIMARY KEY,
	title varchar(20),
	author varchar(20),
	pages int,
	editorial varchar(15)
);

INSERT INTO Book
VALUES ('12345', 'Romeo and Juliet', 'Shakespeare', 3, 'Prentice Hall');

INSERT INTO Vehicle (licenseNumber, brand, model, engineSize, type)
VALUES ('AH-3044', 'Toyota', 'Corolla', 1600, 'SE');

