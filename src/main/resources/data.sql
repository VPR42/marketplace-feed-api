IF NOT EXISTS (SELECT 1 FROM cities) THEN
INSERT INTO cities (region, name)
VALUES ('Ростовская область', 'Ростов-на-Дону');

INSERT INTO users (id, email, password, surname, name, patronymic, avatar_path, created_at, city)
VALUES ('58da1841-6609-4d27-949e-73218ce48922', 'test@mail.ru', '{noop}123456', 'Иванов', 'Иван', 'Иванович', 'https://somephoto.com', now(), 1);

INSERT INTO masters_info (master_id, experience, description, pseudonym, phone_number, working_hours)
VALUES('58da1841-6609-4d27-949e-73218ce48922', 3, 'Привет, я мастер', 'Иван Строит', '0000000000', '8:00-19:00');
