CREATE TABLE Book (
bookId int PRIMARY KEY,
title varchar(30),
pages int,
editorial varchar(30),
authorId int
)
;
CREATE TABLE student (
	studentId int PRIMARY KEY,
	name varchar(30),
	age int,
	code varchar(10),
	credits int
);
SELECT bookId, title, pages, authorId, editorial
FROM Book;  

SELECT *
FROM Author;

SELECT title
FROM Book
WHERE bookId = 1;

SELECT b.title
FROM Book AS b
WHERE pages > 100 AND editorial = 'Prentice Hall';

SELECT *
FROM Book
WHERE authorId = 1 OR pages < 200;

SELECT b.*
FROM Book AS b, Author AS a
WHERE b.authorId = a.authorId AND a.name = 'Michael Crichton';

SELECT bookId, title, pages, name
FROM Book, Author
WHERE Book.authorId = Author.authorId;

SELECT a.name, title
FROM Book, Author AS a
WHERE Book.authorId = a.authorId AND Book.pages > 200;

SELECT a.name
FROM Author AS a, Book AS b
WHERE a.authorId = b.authorId AND b.title = 'Star Wars';

SELECT a.name, b.title
FROM Author AS a, Book AS b
WHERE a.authorId = b.authorId AND a.nationality <> 'Taiwan';

SELECT COUNT(*)
FROM Book;


SELECT COUNT(*)
FROM Author
WHERE nationality = 'Taiwan';

SELECT SUM(pages)
FROM Book
WHERE authorId = 2;


