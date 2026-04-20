# Car Service Auto

## Описание

Микросервисная платформа для автосервиса (B2B).

## Функционал

- Управление клиентами и автомобилями
- Учёт мастеров и специализаций
- Создание и отслеживание заявок на ремонт
- Управление складом запчастей
- Автоматические уведомления
- Справочники (марки авто, услуги, категории запчастей)

## Архитектура

- 7 микросервисов на Spring Boot
- Frontend на React
- PostgreSQL
- Spring Cloud Gateway
- Keycloak для авторизации
- Apache Kafka для событий
- Docker + Kubernetes готовы

## Стек

| Компонент | Технология |
|-----------|-------------|
| Backend | Java 17, Spring Boot 3.3 |
| Frontend | React 18, Tailwind CSS |
| Gateway | Spring Cloud Gateway |
| Database | PostgreSQL |
| Auth | Keycloak |
| Messaging | Apache Kafka |
| Build | Gradle |
| Container | Docker, K8s |

## Сервисы

| Сервис | Порт | Назначение |
|--------|------|-------------|
| Gateway | 8081 | Маршрутизация |
| Claims | 8082 | Заявки |
| Clients | 8083 | Клиенты |
| Masters | 8084 | Мастера |
| Warehouse | 8085 | Склад |
| Notifications | 8086 | Уведомления |
| NSI | 8091 | Справочники |

## Запуск

```bash
docker-compose up -d
```

- Frontend: http://localhost:3000
- Keycloak: http://localhost:8180 (admin/admin)

## Структура

```
car-service-auto/
├── docker/              # Docker файлы
├── docker-compose.yml   # Запуск
├── k8s/               # Kubernetes манифесты
├── car-service-frontend/ # React фронтенд
└── common-*/          # Микросервисы
```

## Автор

Arty (18 лет)

## Ментор

Илья