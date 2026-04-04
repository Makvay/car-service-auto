# Car Service Auto

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-3.5-orange)

## Описание

Микросервисный проект автосервиса с 7 независимыми сервисами, построенный на стеке Java/Spring Boot.

**Особенности:**
- 7 микросервисов с разными зонами ответственности
- Общение через REST API и Apache Kafka
- Единая точка входа через Spring Cloud Gateway
- Своя база данных (схема) для каждого сервиса
- Документация API через Swagger/OpenAPI

## Архитектура

```
                    ┌─────────────────┐
                    │   Gateway       │
                    │    :8080        │
                    └────────┬────────┘
                             │
      ┌──────────────────────┼──────────────────────┐
      │          │           │           │          │
 ┌────┴────┐ ┌────┴────┐ ┌────┴────┐ ┌────┴────┐ ┌────┴────┐
 │ Claim  │ │ Client │ │ Master │ │Warehouse│ │Notification│
 │ :8081  │ │ :8082  │ │ :8083  │ │  :8084  │ │  :8085   │
 └────┬────┘ └────┬────┘ └────┬────┘ └────┬────┘ └────┬────┘
      │          │           │           │          │
      └──────────┴───────────┴─────┬─────┴──────────┘
                                   │
                            ┌──────┴──────┐
                            │    NSI      │
                            │   :8086     │
                            └─────────────┘
```

*Скриншот архитектуры: [Architecture Screenshot](docs/images/architecture.png)*

## Сервисы

| Порт | Сервис | Назначение | База данных |
|------|--------|------------|-------------|
| 8080 | Gateway | Маршрутизация, единый Swagger | - |
| 8081 | Claim | Заявки на ремонт | claim |
| 8082 | Client | Клиенты и автомобили | client |
| 8083 | Master | Мастера, специализации | master |
| 8084 | Warehouse | Склад запчастей | warehouse |
| 8085 | Notification | Уведомления (SMS/Email) | notification |
| 8086 | NSI | Справочники (марки, услуги) | nsi |

## Технологический стек

- **Язык:** Java 17
- **Фреймворк:** Spring Boot 3.3.4
- **Gateway:** Spring Cloud Gateway
- **База данных:** PostgreSQL (отдельные схемы)
- **Message Broker:** Apache Kafka
- **Сборка:** Gradle (мультимодульный проект)
- **Документация:** SpringDoc OpenAPI (Swagger)
- **Межсервисная связь:** Feign Client, REST, Kafka

![Tech Stack](docs/images/tech-stack.png)

## Быстрый старт

### Требования
- JDK 17
- PostgreSQL 15+
- Apache Kafka
- Gradle 9.0+

### Клонирование
```bash
git clone https://github.com/Makvay/car-service-auto.git
cd car-service-auto
```

### Сборка
```bash
./gradlew clean build -x test
```

### Запуск всех сервисов

```bash
# Терминал 1 - Gateway
./gradlew :common-service-parent:gateway-service:bootRun

# Терминал 2 - Claim
./gradlew :common-service-parent:claim-microservice:bootRun

# Терминал 3 - Client
./gradlew :common-service-parent:client-microservice:bootRun

# Терминал 4 - Master
./gradlew :common-service-parent:master-microservice:bootRun

# Терминал 5 - Warehouse
./gradlew :common-service-parent:warehouse-microservice:bootRun

# Терминал 6 - Notification
./gradlew :common-service-parent:notification-microservice:bootRun

# Терминал 7 - NSI
./gradlew :common-service-parent:nsi-microservice:bootRun
```

*Скриншот запуска: [Startup Screenshot](docs/images/startup.png)*

## API Documentation (Swagger)

После запуска открыть Swagger UI:

| Сервис | URL |
|--------|-----|
| Gateway | http://localhost:8080/swagger-ui.html |
| Claim | http://localhost:8081/swagger-ui.html |
| Client | http://localhost:8082/swagger-ui.html |
| Master | http://localhost:8083/swagger-ui.html |
| Warehouse | http://localhost:8084/swagger-ui.html |
| Notification | http://localhost:8085/swagger-ui.html |
| NSI | http://localhost:8086/swagger-ui.html |

*Пример Swagger: [Swagger Screenshot](docs/images/swagger.png)*

## API Endpoints

### Claim Service (8081)
```
POST   /api/v1/claims           - Создать заявку
GET    /api/v1/claims/{id}   - Получить заявку
PUT    /api/v1/claims/{id}/status - Изменить статус
GET    /api/v1/claims         - Список заявок
```

### Client Service (8082)
```
POST   /api/client             - Создать клиента
GET    /api/client/{id}       - Получить клиента
POST   /api/client/vehicle   - Добавить автомобиль
GET    /api/client/{id}/vehicles - Автомобили клиента
PUT    /api/client/vehicle/{id}/mileage - Обновить пробег
```

### Master Service (8083)
```
POST   /api/v1/masters        - Создать мастера
GET    /api/v1/masters        - Список мастеров
GET    /api/v1/masters/{id}   - Мастер по ID
GET    /api/v1/masters/active - Активные мастера
PUT    /api/v1/masters/{id}   - Обновить мастера
DELETE /api/v1/masters/{id}   - Деактивировать
```

### Warehouse Service (8084)
```
POST   /api/v1/parts          - Создать запчасть
GET    /api/v1/parts         - Список запчастей
GET    /api/v1/parts/{id}    - Запчать по ID
PUT    /api/v1/parts/{id}    - Обновить запчать
DELETE /api/v1/parts/{id}   - Удалить запчать

POST   /api/v1/reservations  - Создать резерв
GET    /api/v1/reservations/claim/{claimId} - Резервы по заявке

GET    /api/v1/inventory/part/{partId} - Остаток на складе
PUT    /api/v1/inventory/{id}/quantity - Обновить количество

GET    /api/v1/supplies       - Все поставки
GET    /api/v1/supplies/{id} - Поставка по ID
PUT    /api/v1/supplies/{id}/status - Изменить статус
```

### Notification Service (8085)
```
GET    /api/v1/notification              - Все уведомления
GET    /api/v1/notification/client/{clientId} - Уведомления клиента
```

### NSI Service (8086)
```
GET    /api/nsi/vehicle-brands           - Марки автомобилей
GET    /api/nsi/services               - Виды услуг
GET    /api/nsi/part-categories        - Категории запчастей
GET    /api/nsi/health                 - Проверка здоровья
```

## Kafka События

### Топики

| Топик | Отправитель | Получатель | Описание |
|-------|-------------|------------|----------|
| `client.registered` | Client | Notification | Клиент зарегистрирован |
| `vehicle.registered` | Client | Notification, Warehouse | Авто добавлено |
| `mileage.updated` | Client | - | Пробег обновлён |
| `claim.created` | Claim | Master, Warehouse, Notification | Новая заявка |
| `claim.status.changed` | Claim | Notification | Статус заявки изменён |
| `claim.assigned.to.master` | Claim | Master | Мастер назначен |
| `work.started` | Master | Warehouse | Работы начаты |
| `work.completed` | Master | Notification | Работы завершены |

*Скриншот Kafka: [Kafka Screenshot](docs/images/kafka.png)*

### Пример события (claim.created)
```json
{
  "claimId": 1,
  "clientId": 5,
  "vehicleId": 10,
  "description": "Замена масла",
  "status": "CREATED",
  "createdAt": "2024-01-15T10:30:00"
}
```

## Структура проекта

```
car-service-auto/
├── common-dto-parent/          # Общие DTO
│   ├── claim-dto/
│   ├── client-dto/
│   ├── master-dto/
│   ├── nsi-dto/
│   ├── notification-dto/
│   └── warehouse-dto/
├── common-entity-parent/       # Сущности JPA
│   ├── claim-entity/
│   ├── client-entity/
│   ├── master-entity/
│   ├── nsi-entity/
│   ├── notification-entity/
│   └── warehouse-entity/
├── common-service-parent/       # Микросервисы
│   ├── claim-microservice/     # :8081
│   ├── client-microservice/    # :8082
│   ├── gateway-service/        # :8080
│   ├── master-microservice/    # :8083
│   ├── nsi-microservice/       # :8086
│   ├── notification-microservice/ # :8085
│   └── warehouse-microservice/  # :8084
├── docs/                       # Документация
│   └── images/                  # Скриншоты
└── gradle/                     # Gradle Wrapper
```

## Конфигурация

### База данных

Каждый сервис использует свою схему в PostgreSQL:

```sql
CREATE DATABASE car_service;

-- Схемы создаются автоматически через Flyway
-- claim, client, master, warehouse, notification, nsi
```

### Kafka

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
```

## Тестирование

```bash
# Запустить все тесты
./gradlew test

# Запустить конкретный сервис
./gradlew :common-service-parent:master-microservice:test
```

## Полезные ссылки

- [Swagger UI](http://localhost:8081/swagger-ui.html) - Документация API
- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/)
- [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/)
- [Spring Kafka](https://docs.spring.io/spring-kafka/docs/current/reference/)

## Screenshots

### Запуск сервисов
![Startup](docs/images/startup.png)

### Swagger UI
![Swagger](docs/images/swagger.png)

### Архитектура
![Architecture](docs/images/architecture.png)

---

**Автор:** Makvay  
**Версия:** 1.0.0  
**Лицензия:** MIT