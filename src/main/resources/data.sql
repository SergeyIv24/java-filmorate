INSERT INTO ratings (name)
SELECT 'G'
WHERE (SELECT COUNT(NAME)  FROM RATINGS) < 5;
INSERT INTO ratings (name)
SELECT 'PG'
WHERE (SELECT COUNT(NAME)  FROM RATINGS) < 5;
INSERT INTO ratings (name)
SELECT 'PG-13'
WHERE (SELECT COUNT(NAME)  FROM RATINGS) < 5;
INSERT INTO ratings (name)
SELECT 'R'
WHERE (SELECT COUNT(NAME)  FROM RATINGS) < 5;
INSERT INTO ratings (name)
SELECT 'NC-17'
WHERE (SELECT COUNT(NAME)  FROM RATINGS) < 5;

INSERT INTO status (status_name)
SELECT ('accepted')
WHERE (SELECT COUNT(status_name)  FROM status) < 2;
INSERT INTO status (status_name)
SELECT ('unaccepted')
WHERE (SELECT COUNT(status_name)  FROM status) < 2;

INSERT INTO geners (gener_name)
SELECT('Комедия')
WHERE (SELECT COUNT(gener_name)  FROM geners) < 6;
INSERT INTO geners (gener_name)
SELECT('Драма')
WHERE (SELECT COUNT(gener_name)  FROM geners) < 6;
INSERT INTO geners (gener_name)
SELECT('Мультфильм')
WHERE (SELECT COUNT(gener_name)  FROM geners) < 6;
INSERT INTO geners (gener_name)
SELECT('Триллер')
WHERE (SELECT COUNT(gener_name)  FROM geners) < 6;
INSERT INTO geners (gener_name)
SELECT('Документальный')
WHERE (SELECT COUNT(gener_name)  FROM geners) < 6;
INSERT INTO geners (gener_name)
SELECT('Боевик')
WHERE (SELECT COUNT(gener_name)  FROM geners) < 6;