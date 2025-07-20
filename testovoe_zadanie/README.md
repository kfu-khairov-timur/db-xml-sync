# Тестовое задание: Синхронизация БД с XML

## 📌 Описание

Java-приложение, выполняющее две основные функции:

1. **Экспорт содержимого таблицы `positions` в XML-файл**
2. **Синхронизация таблицы `positions` по XML-файлу**

Приложение использует DOM для работы с XML, JDBC для работы с БД, и логирует все этапы в файл через SLF4J + Logback.

---

## ⚙️ Используемые технологии

- Java 17
- Gradle
- PostgreSQL (JDBC)
- SLF4J + Logback
- XML DOM (чтение и запись)
- HashMap / HashSet
- Логирование в файл
- Конфигурация через `application.properties`

---

## 📂 Структура проекта


testovoe_zadanie/
├── build.gradle
├── README.md
├── schema.sql
├── run-export.sh
├── run-sync.sh
├── src/
│   └── main/
│       ├── java/com/example/testovoe_zadanie/
│       │    ├── Main.java
│       │    ├── XmlExporter.java
│       │    ├── XmlSynchronizer.java
│       │    └── model/
│       │         ├── Position.java
│       │         └── PositionKey.java
│       └── resources/
│           ├── application.properties
│           └── logback.xml


---

## 📦 Сборка проекта

bash
./gradlew clean build

---

---

## 📦 Запуск

Экспорт таблицы в XML

java -jar build/libs/testovoe_zadanie-0.0.1-SNAPSHOT.jar export output.xml

Синхронизация таблицы по XML

java -jar build/libs/testovoe_zadanie-0.0.1-SNAPSHOT.jar sync output.xml

Или использовать скрипты:
./run-export.sh
./run-sync.sh

### 🚀 Запуск в Docker

bash
docker build -t testovoe-zadanie .
docker run --rm -v "$PWD:/output" testovoe-zadanie export /output/output.xml
docker run --rm -v "$PWD:/logs" testovoe-zadanie cat logs/app.log


## ⚙️ Настройки

Все параметры указываются в src/main/resources/application.properties:
db.url=jdbc:postgresql://localhost:5432/testovoe_zadanie
db.user=postgres
db.password=your_password
log.file=logs/app.log


## 📄 SQL-структура

Файл schema.sql:

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



## 📌 Особенности


Сравнение данных идёт по dep_code + dep_job (натуральный ключ)
Все операции выполняются в одной транзакции
Дубликаты в XML приводят к ошибке
Все действия логируются в logs/app.log



## 👨‍💻 Личный комментарий

Это моя реализация тестового задания. Основное внимание я уделил корректной работе с транзакциями, чистоте XML и минимизации обращений к БД.