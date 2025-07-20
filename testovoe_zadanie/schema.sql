CREATE TABLE positions (
                           id SERIAL PRIMARY KEY,
                           dep_code VARCHAR(20) NOT NULL,
                           dep_job VARCHAR(100) NOT NULL,
                           description VARCHAR(255),
                           UNIQUE (dep_code, dep_job)
);

INSERT INTO positions (dep_code, dep_job, description) VALUES
                                                           ('A001', 'Инженер', 'Занимается разработкой'),
                                                           ('A002', 'Тестировщик', 'Проверка качества'),
                                                           ('A003', 'Менеджер', 'Руководит проектами'),
                                                           ('A004', 'DevOps', 'CI/CD и инфраструктура'),
                                                           ('A005', 'HR', 'Подбор персонала'),
                                                           ('A006', 'Аналитик', 'Работа с данными'),
                                                           ('A007', 'Data Scientist', 'ML и предсказания'),
                                                           ('A008', 'QA Lead', 'Организация тестирования'),
                                                           ('A009', 'Tech Writer', 'Документация'),
                                                           ('A010', 'Product Owner', 'Планирование продукта'),
                                                           ('A011', 'Designer', 'UI/UX дизайн'),
                                                           ('A012', 'Security Engineer', 'Защита данных'),
                                                           ('A013', 'Support', 'Техподдержка клиентов');