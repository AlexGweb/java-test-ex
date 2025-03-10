# Library Management System

Система управления библиотекой с JWT-аутентификацией и ролевым доступом.

## Требования

- Java 17 или выше
- Maven 3.6 или выше
- PostgreSQL 12 или выше
- IDE (рекомендуется IntelliJ IDEA)

## Настройка базы данных

1. Создайте базу данных в PostgreSQL:
```sql
CREATE DATABASE library_db;
```

2. Настройте подключение в `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/library_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Запуск проекта

1. Клонируйте репозиторий:
```bash
git clone https://github.com/your-username/library-system.git
cd library-system
```

2. Соберите проект:
```bash
mvn clean install
```

3. Запустите приложение:
```bash
mvn spring-boot:run
```

Приложение будет доступно по адресу: `http://localhost:8080/api`

## Тестовые пользователи

В системе предустановлены следующие пользователи:

1. **Администратор**:
   - Username: admin
   - Password: test123
   - Role: ADMIN

2. **Библиотекарь**:
   - Username: librarian
   - Password: test123
   - Role: LIBRARIAN

3. **Читатель**:
   - Username: reader
   - Password: test123
   - Role: READER

## API Endpoints

### Аутентификация

#### Вход в систему
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "admin",
    "password": "test123"
}
```

#### Регистрация нового пользователя
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "password123"
}
```

### Защищенные эндпоинты

#### Публичные эндпоинты (доступны всем):
- `GET /api/public/**` - публичная информация
- `/api/swagger-ui/**` - документация API

#### Эндпоинты администратора (только ADMIN):
- `GET /api/admin/**` - административные функции
- `DELETE /**` - удаление ресурсов

#### Эндпоинты библиотекаря (ADMIN и LIBRARIAN):
- `GET /api/librarian/**` - функции библиотекаря
- `POST /api/books/**` - добавление книг
- `PUT /api/books/**` - обновление информации о книгах

#### Эндпоинты читателя (все аутентифицированные пользователи):
- `GET /api/books/**` - просмотр книг
- `GET /api/reader/**` - функции читателя

## Примеры использования

### 1. Получение JWT токена (вход)
```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"admin","password":"test123"}'
```

### 2. Доступ к защищенному ресурсу
```bash
curl -X GET http://localhost:8080/api/books \
-H "Authorization: Bearer your-jwt-token"
```

### 3. Добавление новой книги (требуется роль LIBRARIAN или ADMIN)
```bash
curl -X POST http://localhost:8080/api/books \
-H "Authorization: Bearer your-jwt-token" \
-H "Content-Type: application/json" \
-d '{
    "title": "New Book",
    "author": "Author Name",
    "isbn": "1234567890"
}'
```

## Безопасность

- Все пароли хешируются с использованием BCrypt
- Используется JWT для аутентификации
- Настроена защита от CSRF атак
- Настроен CORS для работы с фронтендом

## Разработка

### Добавление новых эндпоинтов

1. Создайте контроллер в пакете `controller`
2. Добавьте необходимые методы
3. Используйте аннотации для защиты:
```java
@PreAuthorize("hasRole('ADMIN')")
public void adminMethod() { }
```

### Логирование

Уровни логирования настраиваются в `application.properties`:
```properties
logging.level.org.springframework.security=DEBUG
logging.level.com.library=DEBUG
``` 